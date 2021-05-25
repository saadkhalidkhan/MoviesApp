package com.example.hafiz_saad.movies.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hafiz_saad.movies.R;
import com.example.hafiz_saad.movies.controller.services.GetMovieDetails;
import com.example.hafiz_saad.movies.model.GetDetailsModel.GetDetails;
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result;
import com.example.hafiz_saad.movies.controller.services.Util.PaginationScrollListener;
import com.example.hafiz_saad.movies.view.adapters.RecycleAdapter;
import com.example.hafiz_saad.movies.view.adapters.RecyclerItemClickListener;
import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject;
import com.example.hafiz_saad.movies.model.VideoModel.VideoRootObject;
import com.example.hafiz_saad.movies.controller.services.GenreService;
import com.example.hafiz_saad.movies.controller.services.NowPlayingService;
import com.example.hafiz_saad.movies.controller.services.VideoService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private String nowPlaying_URL;
    public static RootObject rootObject;
    private String genre_URL;
    public static int index;
    public static String imageBase_URL;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    private GenreService genreService;

    public static VideoRootObject videoRootObject;
    private VideoService videoService;
    private GetMovieDetails movieDetails;
    private Subscription subscription;
    public static boolean check = false;
    private NowPlayingService nowPlayingService;
    private String vid_URL = "https://api.themoviedb.org/3/";
    public static List<Result> results, result,total_results;
    private int result_index = 0;
    private int total_size = 0;
    public static GetDetails details;
    private static final String TAG = MainActivity.class.getSimpleName();
//    public static RootObjectGenre rootObjectGenre;
    private LinearLayoutManager linearLayoutManager;
    //pagination
    private static final int PAGE_START = 0;
    private View sharedView;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private float TOTAL_PAGES = 5;
    private int i = 1;
    private float currentPage = PAGE_START;
    private ProgressBar progressBar;
    private boolean checked = false;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressDialog = new ProgressDialog(MainActivity.this);
        result = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Recycler", String.valueOf(view.getId()));
                progressDialog.setMessage("Loading.....");
                progressDialog.show();
                index = position;
                sharedView = view;
                retrofitInstanceDetails();
                getRequestDetails(result.get(position).getId());
                result_index = position;

            }
        }));
        recyclerView.addOnScrollListener(new PaginationScrollListener(getApplicationContext(),linearLayoutManager){
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to next one
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRequestNowPlaying();
                    }
                },1500);
            }

            @Override
            public float getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean lastPage() {
                return isLastPage;
            }

            @Override
            public boolean loading() {
                return isLoading;
            }

        } );


        init_URLs();
        retrofitInstanceNowPlaying();
        getRequestNowPlaying();
    }

    private void init_URLs() {
        nowPlaying_URL = "https://api.themoviedb.org/3/";
        genre_URL = "https://api.themoviedb.org/3/";
        imageBase_URL = "http://image.tmdb.org/t/p/w185//";
    }

    private void retrofitInstanceNowPlaying() {

        final Gson gson =
                new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        retrofit = new Retrofit.Builder().baseUrl(nowPlaying_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        nowPlayingService = retrofit.create(NowPlayingService.class);
    }

    private void getRequestNowPlaying() {
        Call<RootObject> call = nowPlayingService.getData("upcoming",""+i);
        call.enqueue(new Callback<RootObject>() {
            @Override
            public void onResponse(Call<RootObject> call, final Response<RootObject> response) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        rootObject = response.body();

                        results = rootObject.getResults();
                        currentPage = i;
                        TOTAL_PAGES = i;

                        if (i == 1){
                            first_load();
                        }
                        else
                            next_load();
                        i++;
                    }

                    Log.d("MainActivity", "OnResponse" + response.body());
                }
            }
            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {
                Log.d("MainActivity", "OnFailure");
            }

        });
    }
    private void first_load(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycleAdapter = new RecycleAdapter(getApplicationContext(), result, rootObject, results.size());
                recyclerView.setAdapter(recycleAdapter);
                loadFirstPage();
            }
        },1500);

    }
    private void next_load(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextPage();

            }
        },1500);

    }

private void retrofitInstanceDetails() {
    final OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new LoggingInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();
    final Gson gson =
            new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    retrofit = new Retrofit.Builder().baseUrl(vid_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    movieDetails = retrofit.create(GetMovieDetails.class);
}

    private void getRequestDetails(String id) {
        Call<GetDetails> call = movieDetails.getData(id);
        call.enqueue(new Callback<GetDetails>() {
            @Override
            public void onResponse(Call<GetDetails> call, Response<GetDetails> response) {
                if(response.code()==200) {
                    details = response.body();
                    retrofitInstanceVideo();
                    getRequestVideo(result.get(result_index).getId());

                    Log.d("MainActivity", "OnResponse" + response.body());
                }
            }

            @Override
            public void onFailure(Call<GetDetails> call, Throwable t) {
                Log.d("MainActivity","OnFailure");
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void retrofitInstanceVideo() {
        final OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        final Gson gson =
                new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        retrofit = new Retrofit.Builder().baseUrl(vid_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        videoService = retrofit.create(VideoService.class);
    }

    private void getRequestVideo(String id) {
        Call<VideoRootObject> call = videoService.getData(id);
        call.enqueue(new Callback<VideoRootObject>() {
            @Override
            public void onResponse(Call<VideoRootObject> call, Response<VideoRootObject> response) {
                if(response.code()==200) {
                    videoRootObject = response.body();
                    check = true;
                    progressDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Intent intent = new Intent(MainActivity.this, TransitionActivity.class);
                    Pair<View, String> pair = Pair.create(sharedView.findViewById(R.id.poster),sharedView.findViewById(R.id.poster).getTransitionName());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,pair);
                    startActivity(intent,optionsCompat.toBundle());
                    }
                    Log.d("MainActivity", "OnResponse" + response.body());
                }
            }

            @Override
            public void onFailure(Call<VideoRootObject> call, Throwable t) {
                Log.d("MainActivity","OnFailure");
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void loadFirstPage(){

        for(int i = 0; i< results.size(); i++){
                result.add(results.get(i));
                result_index++;
        }
        recycleAdapter.notifyDataSetChanged();
        if(currentPage <= TOTAL_PAGES) {

        }
        else {
            isLastPage = true;
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show();
//            isLoading = true;
        }
    }

    private void loadNextPage(){
        for(int i = 0; i< results.size(); i++){
                result.add(results.get(i));
                result_index++;
        }
        recycleAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        isLoading = false;

        if(currentPage <= TOTAL_PAGES ) {

        }
        else {
            isLastPage = true;
//            isLoading = true;
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show();
        }
    }
}
