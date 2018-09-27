package com.example.hafiz_saad.movies.controller.services;

import com.example.hafiz_saad.movies.model.GenreModel.RootObjectGenre;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GenreService {
    @GET("genre/movie/list?api_key=3d1f0d7184901446cb440348e5e2d2e3")
    Call<RootObjectGenre> getData();
}
