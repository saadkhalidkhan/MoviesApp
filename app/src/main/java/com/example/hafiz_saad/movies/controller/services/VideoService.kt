package com.example.hafiz_saad.movies.controller.services

import com.example.hafiz_saad.movies.model.VideoModel.VideoRootObject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoService {

    @GET("movie/{id}/videos?api_key=3d1f0d7184901446cb440348e5e2d2e3")
    fun getData(@Path("id") movieId: String): Call<VideoRootObject>
    //    Observable<VideoRootObject> getData(@Path("id") String id);
}
