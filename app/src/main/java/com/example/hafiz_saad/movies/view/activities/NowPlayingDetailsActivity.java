package com.example.hafiz_saad.movies.view.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hafiz_saad.movies.R;
import com.example.hafiz_saad.movies.controller.services.VideoService;
import com.example.hafiz_saad.movies.model.GetDetailsModel.GetDetails;
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result;
import com.example.hafiz_saad.movies.model.VideoModel.VideoRootObject;
import com.example.hafiz_saad.movies.controller.services.API.YoutubeConfig;
import com.example.hafiz_saad.movies.view.adapters.RecyclerItemClickListener;
import com.example.hafiz_saad.movies.view.adapters.ShowImagesAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingDetailsActivity extends  YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener, CompoundButton.OnCheckedChangeListener,
        YouTubePlayer.OnFullscreenListener {

    YouTubePlayerView youTubePlayerView;
    private Retrofit retrofit;
    private LinearLayout relativeLayout;
    private VideoRootObject videoRootObject;
    private VideoService videoService;
    ProgressDialog progressDialog;
    private String vid_URL = "https://api.themoviedb.org/3/";
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private String imageBase_URL = "http://image.tmdb.org/t/p/w185//";
    private List<Result> results;
//    RootObjectGenre rootObjectGenre;
    private GetDetails details;
    private ImageView poster;
    private TextView title,releaseDate,genre,overview,country,company,rating;
    StringBuilder genreData, countryData,companyData;
    public static String videoKey;
    float currentProgress = 0;
    private ShowImagesAdapter showImagesAdapter;
    boolean check = false, checking = false,checked = false;
//    private YouTubePlayer player = null;
    private YouTubePlayer mPlayer = null;
    private RecyclerView recyclerView;
    int position, l = -1 , k = 0, p =0, count = 0, z  = 0;
    public static  int value = 0 ;
    String key;
    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView;
    ImageButton play,pause, fullscreen, smallscreen;
    private Handler mHandler = null;
    private SeekBar mSeekBar;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_details);
        //Add play button to explicitly play video in YouTubePlayerView
//        if(savedInstanceState!= null){
//            value = savedInstanceState.getInt("value");
//            getScreenOrientation();
//        }
        progressDialog = new ProgressDialog(NowPlayingDetailsActivity.this);
        mPlayButtonLayout = findViewById(R.id.video_control);
        play =(ImageButton) findViewById(R.id.play_video);
        pause = (ImageButton) findViewById(R.id.pause_video);
        fullscreen = (ImageButton) findViewById(R.id.fullscreen);
        smallscreen = (ImageButton) findViewById(R.id.small);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        country = (TextView) findViewById(R.id.country);
        company = (TextView) findViewById(R.id.company);
        rating = (TextView) findViewById(R.id.rating);
        fullscreen.setOnClickListener(this);
        smallscreen.setOnClickListener(this);
        pause.setOnClickListener(this);
        play.setOnClickListener(this);

        relativeLayout = (LinearLayout) findViewById(R.id.relative);
        poster = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        releaseDate = (TextView) findViewById(R.id.release_date);
        genre = (TextView) findViewById(R.id.genre);
        overview = (TextView) findViewById(R.id.overview);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlay);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mPlayTimeTextView = (TextView) findViewById(R.id.play_time);
        mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        position = MoviesActivity.getIndex();
       this.details = MoviesActivity.getDetails();
       videoRootObject = MoviesActivity.getVideoRootObject();

       results = MoviesActivity.getResults();

        genreData = new StringBuilder(details.getGenres().size());
        for(int i = 0; i< details.getGenres().size(); i++){
            genreData.append(details.getGenres().get(i).getName() + " ");
        }
        companyData = new StringBuilder(details.getProductionCompanies().size());
        for(int i = 0; i< details.getProductionCompanies().size(); i++){
            if(i == details.getProductionCompanies().size()-1)
                companyData.append(details.getProductionCompanies().get(i).getName());
            else
            companyData.append(details.getProductionCompanies().get(i).getName() + ", ");
        }
        countryData= new StringBuilder(details.getProductionCountries().size());
        for(int i = 0; i< details.getProductionCountries().size(); i++){
            if(i == details.getProductionCountries().size()-1)
                countryData.append(details.getProductionCountries().get(i).getName());
            else
            countryData.append(details.getProductionCountries().get(i).getName() + ", ");
        }
//        for(int j=0; j<results.get(position).getGenreIds().size();  j++) {
//            for (int i = 0; i < rootObjectGenre.getGenres().size(); i++) {
//                if (results.get(position).getGenreIds().get(j).equals(rootObjectGenre.getGenres().get(i).getId())) {
//                    genreData.append(rootObjectGenre.getGenres().get(i).getName() + " ");
//                    break;
//                }
//            }
//        }
        setDetails();
        for(int i =0; i< videoRootObject.getResults().size(); i++){
                    if(videoRootObject.getResults().get(i).getType().equals("Trailer")){
                        key = videoRootObject.getResults().get(i).getKey();
                        videoKey = key;
                        break;
                    }
                }
        youTubePlayerView.initialize(YoutubeConfig.getApiKey(), this);

        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);

        mHandler = new Handler();
        recycling();

//        savedInstanceState.putInt("value",value);

    }
    public void recycling(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPlayer.pause();
                progressDialog.setMessage("Loading.....");
                progressDialog.show();
                retrofitInstanceVideo();
                getRequestVideo(results.get(position).getId());
            }
        }));


        showImagesAdapter = new ShowImagesAdapter(getApplicationContext(), results, results.size());

        recyclerView.setAdapter(showImagesAdapter);
    }
    public void setDetails(){
        rating.setText(details.getVoteAverage()+"/10");
        if(!countryData.equals(""))
            country.setText(countryData);
        else
            country.setText("Not Available");
        if(!companyData.equals(""))
            company.setText(companyData);
        else
            company.setText("Not Available");
        genre.setText(genreData);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageBase_URL+this.results
                .get(position)
                .getPosterPath(),poster);
        title.setText(this.results.get(position).getTitle());
        releaseDate.setText(this.results.get(position).getReleaseDate());
        overview.setText(this.results.get(position).getOverview());
    }

    @Override
    public void onBackPressed() {
//        mPlayer.release();
//        key = null;
//        mPlayer.setFullscreen(false);
//
//        videoRootObject = null;
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
                if (null != mPlayer && !mPlayer.isPlaying()) {
                    mPlayer.play();
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pause_video:
                if (null != mPlayer && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    pause.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fullscreen:
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getScreenOrientation();
                    fullscreen.setVisibility(View.GONE);
                    smallscreen.setVisibility(View.VISIBLE);
                break;
            case R.id.small:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getScreenOrientation();
                fullscreen.setVisibility(View.VISIBLE);
                smallscreen.setVisibility(View.GONE);
                break;
        }
    }
    private void displayCurrentTime() {

        if ( mPlayer == null) return;

        getScreenOrientation();


        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);

        mSeekBar.setMax(mPlayer.getDurationMillis());
//            currentProgress= (((float) mPlayer.getCurrentTimeMillis() /(float)  (mPlayer.getDurationMillis())) * 100);
            Log.d("progress",String.valueOf(currentProgress));
//            mSeekBar.setProgress((int) currentProgress);
            mSeekBar.setProgress(mPlayer.getCurrentTimeMillis());
            value = mSeekBar.getProgress();
            check = true;

    }
    public void getScreenOrientation()
    {

        int val=0;
        val = this.getResources().getConfiguration().orientation;

        if (val == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
            p.weight = (float) 0.7;
            scrollView.setVisibility(View.GONE);
            relativeLayout.setLayoutParams(p);
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p1.weight = (float) 0.15;
            mPlayButtonLayout.setLayoutParams(p1);
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p2.weight = (float) 1.1;
            scrollView.setLayoutParams(p2);
//            relativeLayout.getLayoutParams();
//            p.weight = 0.7f;
            scrollView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
//            relativeLayout.setLayoutParams(p);

        }

        if (val == Configuration.ORIENTATION_LANDSCAPE) {
//                Intent intent = new Intent(NowPlayingDetailsActivity.this,FullScreen.class);
//                startActivity(intent);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
            p.weight = (float) 1.7;
            scrollView.setVisibility(View.GONE);
            relativeLayout.setLayoutParams(p);
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p1.weight = (float) 0.3;
            mPlayButtonLayout.setLayoutParams(p1);
            recyclerView.setVisibility(View.GONE);



        }
    }

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
//            mPlayer.seekToMillis((int) lengthPlayed);
//            if(check == true) {
//                if (k != l && l>0) {
//                    mPlayer.seekToMillis((int) lengthPlayed);
//                    Log.d("checking",String.valueOf(progress));
//                    l = k - 1;
//                    k = progress;
//                }
//                check = false;
//            }
//            else{
//                p = seekBar.getProgress();
//                mPlayer.seekToMillis((int) lengthPlayed);
////                l = k - 1;
////                k = progress;
//                checkdrop = true;
//            }
//            if(check == true){
//                check = false;
//            }
////            else if(checking == true){
////                checking = false;
////            }
//            else {
////                mPlayer.seekToMillis((int) lengthPlayed)
//                checkdrop = true;
////                p = mPlayer.getCurrentTimeMillis();
////                z = mPlayer.getDurationMillis();
////                mSeekBar.setProgress(seekBar.getProgress());
//            }
////                else if(check = true){
////                    mSeekBar.setProgress(progress);
////                }
//
//            Log.d("progresschange",String.valueOf(progress));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d("touch","Touched");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            Toast.makeText(getApplicationContext(),String.valueOf(seekBar.getProgress()),Toast.LENGTH_LONG).show();
            Log.d("touch","Touched Stop");
            Log.d("touch",mPlayer.getCurrentTimeMillis() + " " + mPlayer.getDurationMillis());
            Log.d("touch","Stop Touched");
            mPlayer.seekToMillis(seekBar.getProgress());
            mSeekBar.setProgress(seekBar.getProgress());
            checking = true;
            p = seekBar.getProgress();
            value = mSeekBar.getProgress();

        }
    };
    @NonNull
    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        return (hours == 0 ? "" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (null == player) return;
//        player.setFullscreen(true);
         mPlayer = player;
        displayCurrentTime();

        // Start buffering
//            player.cueVideo(videoKey);
        player.loadVideo(videoKey);

        player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mPlayButtonLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(mPlayerStateChangeListener);
        player.setPlaybackEventListener(mPlaybackEventListener);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };
    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
            Log.d("Youtube","Buffering");
//            p = mPlayer.getCurrentTimeMillis();
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            Log.d("Youtube","onPuased");
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
            if(checking == true){
                if(mSeekBar.getProgress()!=p){
                    mPlayer.seekToMillis(p);
                    mSeekBar.setProgress(p);
                    checking= false;
                }
            }
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
            Log.d("Youtube","onPlaying");
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
//            displayCurrentTime();
            Log.d("Youtube","onSeekTo");
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
            Log.d("Youtube","onStopped");
        }
    };

    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
            Log.d("Youtube","onAdStarted");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            Log.d("Youtube","onError");
        }

        @Override
        public void onLoaded(String arg0) {
            Log.d("Youtube","onError");
        }

        @Override
        public void onLoading() {
            Log.d("Youtube","onLoading");
        }

        @Override
        public void onVideoEnded() {
            Log.d("Youtube","onVideoEnded");
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
//            k=0;
//            l=-1;
        }

        @Override
        public void onVideoStarted() {
//            if(MainActivity.check == true){
//                MainActivity.check = false;
//            }
//            if(FullScreen.checked == true){
////                mSeekBar.setProgress(FullScreen.seekValue);
//                mPlayer.seekToMillis(FullScreen.seekValue);
//                FullScreen.checked = false;
//            }
            Log.d("Youtube","onVideoStarted");
            displayCurrentTime();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onFullscreen(boolean b) {

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
                    progressDialog.dismiss();
                    for(int i =0; i< videoRootObject.getResults().size(); i++){
                        if(videoRootObject.getResults().get(i).getType().equals("Trailer")){
                            mPlayer.loadVideo(videoRootObject.getResults().get(i).getKey());
                            break;
                        }
                    }


                    Log.d("Movies Activity", "OnResponse" + response.body());
                }
            }

            @Override
            public void onFailure(Call<VideoRootObject> call, Throwable t) {
                Log.d("Movies Activity","OnFailure");
                Toast.makeText(NowPlayingDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
