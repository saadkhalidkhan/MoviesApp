package com.example.hafiz_saad.movies.controller.services;

import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NowPlayingService {
//    @FormUrlEncoded
//    @GET("movie/now_playing?api_key=3d1f0d7184901446cb440348e5e2d2e3")
//    @GET("movie/popular?api_key=3d1f0d7184901446cb440348e5e2d2e3")
//    @GET("movie/top_rated?api_key=3d1f0d7184901446cb440348e5e2d2e3")
//    @GET("movie/upcoming?api_key=3d1f0d7184901446cb440348e5e2d2e3")
    @GET("movie/{type}?api_key=3d1f0d7184901446cb440348e5e2d2e3")
    Call<RootObject>getData(@Path("type") String type , @Query("page") String videoId);
//    Call<List<RootObject>> savePost(@Field("Student_ID") String Student_ID);
//    Call<List<RootObject>>getData(@Field("Results") String results, @Field("Page") String page, @Field("Total_Results") String total_results, @Field("Dates") String dates, @Field("Total_Pages") String total_pages);
}
