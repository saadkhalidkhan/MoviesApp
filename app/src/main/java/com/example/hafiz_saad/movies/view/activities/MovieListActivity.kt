package com.example.hafiz_saad.movies.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import com.example.hafiz_saad.movies.R
import com.example.hafiz_saad.movies.controller.services.GetMovieDetails
import com.example.hafiz_saad.movies.model.GetDetailsModel.GetDetails
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result
import com.example.hafiz_saad.movies.controller.services.Util.PaginationScrollListener
import com.example.hafiz_saad.movies.view.adapters.RecycleAdapter
import com.example.hafiz_saad.movies.view.adapters.RecyclerItemClickListener
import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject
import com.example.hafiz_saad.movies.model.VideoModel.VideoRootObject
import com.example.hafiz_saad.movies.controller.services.GenreService
import com.example.hafiz_saad.movies.controller.services.NowPlayingService
import com.example.hafiz_saad.movies.controller.services.VideoService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscription

class MovieListActivity : AppCompatActivity() {

    private var nowPlaying_URL: String? = null
    private var genre_URL: String? = null
    private var retrofit: Retrofit? = null
    private var recyclerView: RecyclerView? = null
    private var recycleAdapter: RecycleAdapter? = null
    private val genreService: GenreService? = null
    private var videoService: VideoService? = null
    private var movieDetails: GetMovieDetails? = null
    private val subscription: Subscription? = null
    private var nowPlayingService: NowPlayingService? = null
    private val vid_URL = "https://api.themoviedb.org/3/"
    private var result_index = 0
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 5f
    private var i = 1
    private var currentPage = PAGE_START.toFloat()
    private var progressBar: ProgressBar? = null
    private val checked = false
    internal var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        recyclerView = findViewById<View>(R.id.recycle) as RecyclerView
        progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        progressDialog = ProgressDialog(this@MovieListActivity)
        result = ArrayList()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(applicationContext, RecyclerItemClickListener.OnItemClickListener { view, position ->
            Log.d("Recycler", view.id.toString())
            progressDialog!!.setMessage("Loading.....")
            progressDialog!!.show()
            index = position
            retrofitInstanceDetails()
            getRequestDetails(result!![position].id)
            result_index = position
        }))
        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(applicationContext, linearLayoutManager) {
            override fun loadMoreItems() {
                this@MovieListActivity.isLoading = true
                currentPage += 1f //Increment page index to next one
                progressBar!!.visibility = View.VISIBLE
                Handler().postDelayed({ getRequestNowPlaying() }, 1500)
            }

            override fun getTotalPageCount(): Float {
                return TOTAL_PAGES
            }

            override fun lastPage(): Boolean {
                return isLastPage
            }

            override fun loading(): Boolean {
                return isLoading
            }

        })


        init_URLs()
        //        retrofitInstanceGenres();
        //        getRequestGenres();
        retrofitInstanceNowPlaying()
        getRequestNowPlaying()
    }

    private fun init_URLs() {
        nowPlaying_URL = "https://api.themoviedb.org/3/"
        genre_URL = "https://api.themoviedb.org/3/"
        imageBase_URL = "http://image.tmdb.org/t/p/w185//"
    }

    private fun retrofitInstanceNowPlaying() {
        val gson = GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        retrofit = Retrofit.Builder().baseUrl(nowPlaying_URL!!)
                //                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        nowPlayingService = retrofit!!.create(NowPlayingService::class.java)
    }

    private fun getRequestNowPlaying() {
        val call = nowPlayingService!!.getData("" + i,"upcoming")
        call.enqueue(object : Callback<RootObject> {
            override fun onResponse(call: Call<RootObject>, response: Response<RootObject>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        rootObject = response.body()

                        results = rootObject!!.results
                        currentPage = i.toFloat()
                        TOTAL_PAGES = i.toFloat()

                        if (i == 1) {
                            first_load()
                        } else
                            next_load()
                        i++
                    }

                    Log.d("MainActivity", "OnResponse" + response.body()!!)
                }
            }
            override fun onFailure(call: Call<RootObject>, t: Throwable) {
                Log.d("MainActivity", "OnFailure")
            }

        })
    }
    private fun first_load() {
        Handler().postDelayed({
            recycleAdapter = RecycleAdapter(applicationContext, result, rootObject, results!!.size)
            recyclerView!!.adapter = recycleAdapter
            loadFirstPage()
        }, 1500)

    }

    private fun next_load() {
        Handler().postDelayed({ loadNextPage() }, 1500)

    }

    private fun retrofitInstanceDetails() {
        val client = OkHttpClient.Builder()
                //                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        val gson = GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        retrofit = Retrofit.Builder().baseUrl(vid_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        movieDetails = retrofit!!.create(GetMovieDetails::class.java)
    }

    private fun getRequestDetails(id: String?) {
        val call = movieDetails!!.getData(id!!)
        call.enqueue(object : Callback<GetDetails> {
            override fun onResponse(call: Call<GetDetails>, response: Response<GetDetails>) {
                if (response.code() == 200) {
                    details = response.body()
                    retrofitInstanceVideo()
                    getRequestVideo(result!![result_index].id)

                    Log.d("MainActivity", "OnResponse" + response.body()!!)
                }
            }

            override fun onFailure(call: Call<GetDetails>, t: Throwable) {
                Log.d("MainActivity", "OnFailure")
                Toast.makeText(this@MovieListActivity, "No Data Found", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun retrofitInstanceVideo() {
        val client = OkHttpClient.Builder()
                //                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        val gson = GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        retrofit = Retrofit.Builder().baseUrl(vid_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        videoService = retrofit!!.create(VideoService::class.java)
    }

    private fun getRequestVideo(id: String?) {
        val call = videoService!!.getData(id!!)
        call.enqueue(object : Callback<VideoRootObject> {
            override fun onResponse(call: Call<VideoRootObject>, response: Response<VideoRootObject>) {
                if (response.code() == 200) {
                    videoRootObject = response.body()
                    check = true
                    progressDialog?.dismiss()
                    val intent = Intent(this@MovieListActivity, NowPlayingDetailsActivity::class.java)
                    startActivity(intent)

                    Log.d("MainActivity", "OnResponse" + response.body()!!)
                }
            }

            override fun onFailure(call: Call<VideoRootObject>, t: Throwable) {
                Log.d("MainActivity", "OnFailure")
                Toast.makeText(this@MovieListActivity, "No Data Found", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun loadFirstPage() {

        for (i in results!!.indices) {
            result?.add(results!![i])
            result_index++
        }
        recycleAdapter!!.notifyDataSetChanged()
        if (currentPage <= TOTAL_PAGES) {

        } else {
            isLastPage = true
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show()
            //            isLoading = true;
        }
    }

    private fun loadNextPage() {
        for (i in results!!.indices) {
            result?.add(results!![i])
            result_index++
        }
        recycleAdapter!!.notifyDataSetChanged()
        progressBar!!.visibility = View.GONE
        isLoading = false

        if (currentPage <= TOTAL_PAGES) {

        } else {
            isLastPage = true
            //            isLoading = true;
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var rootObject: RootObject? = null
        var index: Int = 0
        var imageBase_URL: String = ""

        var videoRootObject: VideoRootObject? = null
        var check = false
        var results: List<Result>? = null
        var result: MutableList<Result>? = null
        var total_results: List<Result>? = null
        var details: GetDetails? = null
        private val TAG = MainActivity::class.java.simpleName
        //pagination
        private val PAGE_START = 0
    }
}
