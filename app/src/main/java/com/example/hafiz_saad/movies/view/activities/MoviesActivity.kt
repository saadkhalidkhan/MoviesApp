package com.example.hafiz_saad.movies.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.hafiz_saad.movies.R
import com.example.hafiz_saad.movies.controller.services.GenreService
import com.example.hafiz_saad.movies.controller.services.GetMovieDetails
import com.example.hafiz_saad.movies.controller.services.NowPlayingService
import com.example.hafiz_saad.movies.controller.services.Util.PaginationScrollListener
import com.example.hafiz_saad.movies.controller.services.VideoService
import com.example.hafiz_saad.movies.model.GetDetailsModel.GetDetails
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result
import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject
import com.example.hafiz_saad.movies.model.VideoModel.VideoRootObject
import com.example.hafiz_saad.movies.view.adapters.RecycleAdapter
import com.example.hafiz_saad.movies.view.adapters.RecyclerItemClickListener
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.app_bar_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscription
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MoviesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var nowPlaying_URL: String? = null
    private var genre_URL: String? = null
    private var retrofit: Retrofit? = null
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
    private var currentPage = MoviesActivity.PAGE_START.toFloat()
    private val checked = false
    private var type:String = "now_playing"
    internal var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        setSupportActionBar(toolbar)
        progressDialog = ProgressDialog(this@MoviesActivity)
        init_URLs()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle!!.layoutManager = linearLayoutManager
        recycle.itemAnimator = DefaultItemAnimator()
        recycle.addOnItemTouchListener(RecyclerItemClickListener(applicationContext,RecyclerItemClickListener.OnItemClickListener{
            view, position ->
            Log.d("Recycler", view.id.toString())
            progressDialog!!.setMessage("Loading.....")
            progressDialog!!.show()
            index = position
            retrofitInstanceDetails()
            getRequestDetails(MoviesActivity.result!![position].id)
            result_index = position
        }))
        recycle.addOnScrollListener(object:PaginationScrollListener(applicationContext,linearLayoutManager){
            override fun loadMoreItems() {
                this@MoviesActivity.isLoading = true
                currentPage += 1f //Increment page index to next one
                progress_bar!!.visibility = View.VISIBLE
                Handler().postDelayed({ getRequestPlaying() }, 1500)
            }

            override fun getTotalPageCount(): Float {
                return TOTAL_PAGES
            }

            override fun lastPage(): Boolean {
                return isLastPage
            }

            override fun loading(): Boolean {
                return isLoading;
            }

        })
       startActivity()
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }
    fun startActivity(){
        retrofitInstancePlaying()
        getRequestPlaying()
        progressDialog!!.dismiss()

    }
    private fun init_URLs() {
        nowPlaying_URL = "https://api.themoviedb.org/3/"
        genre_URL = "https://api.themoviedb.org/3/"
        imageBase_URL = "http://image.tmdb.org/t/p/w185//"
    }

    private fun retrofitInstancePlaying() {
        val gson = GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        retrofit = Retrofit.Builder().baseUrl(nowPlaying_URL!!)
                //                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        nowPlayingService = retrofit!!.create(NowPlayingService::class.java)
    }

    private fun getRequestPlaying() {
        val call = nowPlayingService!!.getData(type,"" + i)
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

                    //                    recycleAdapter = new RecycleAdapter(getApplicationContext(),results, rootObject, response.body().getResults().size());
                    //                    recyclerView.setAdapter(recycleAdapter);
                    Log.d("MainActivity", "OnResponse" + response.body()!!)
                }
            }
            override fun onFailure(call: Call<RootObject>, t: Throwable) {
                Log.d("Movie Activity", "OnFailure")
            }

        })
    }
    private fun first_load() {
            result = ArrayList()
            recycleAdapter = RecycleAdapter(applicationContext, result, rootObject, results!!.size)
            recycle!!.adapter = recycleAdapter
            loadFirstPage()


    }

    private fun next_load() {
        Handler().postDelayed({ loadNextPage() }, 1500)

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
        progress_bar!!.visibility = View.GONE
        isLoading = false

        if (currentPage <= TOTAL_PAGES) {

        } else {
            isLastPage = true
            //            isLoading = true;
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun retrofitInstanceDetails() {
        val client = OkHttpClient.Builder()
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
                Toast.makeText(this@MoviesActivity, "No Data Found", Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this@MoviesActivity, NowPlayingDetailsActivity::class.java)
                    startActivity(intent)
                    Log.d("MainActivity", "OnResponse" + response.body()!!)
                }
            }

            override fun onFailure(call: Call<VideoRootObject>, t: Throwable) {
                Log.d("MainActivity", "OnFailure")
                Toast.makeText(this@MoviesActivity, "No Data Found", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.movies, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.rate_us -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        result!!.clear()
        when (item.itemId) {
            R.id.now_playing -> {
                // Handle the camera action
                type = "now_playing"
                progressDialog!!.show()
                startActivity()
            }
            R.id.up_coming -> {
                type = "upcoming"
                progressDialog!!.show()
                startActivity()
            }
            R.id.popular -> {
                type = "popular"
                progressDialog!!.show()
                startActivity()
            }
            R.id.top_rated -> {
                type = "top_rated"
                progressDialog!!.show()
                startActivity()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    companion object {
        @JvmStatic var rootObject: RootObject? = null
        @JvmStatic var index: Int = 0
        @JvmStatic var imageBase_URL: String = ""
        @JvmStatic var videoRootObject: VideoRootObject? = null
        @JvmStatic var check = false
        @JvmStatic var results: List<Result>? = null
        @JvmStatic var result: MutableList<Result>? = null
        @JvmStatic var total_results: List<Result>? = null
        @JvmStatic var details: GetDetails? = null
        @JvmStatic private val TAG = MoviesActivity::class.java.simpleName
        //pagination
        @JvmStatic private val PAGE_START = 0
    }
}
