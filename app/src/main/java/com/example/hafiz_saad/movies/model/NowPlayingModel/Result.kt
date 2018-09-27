package com.example.hafiz_saad.movies.model.NowPlayingModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Result {
    @SerializedName("vote_count")
    @Expose
    var voteCount: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("video")
    @Expose
    var video: String? = null

    @SerializedName("vote_average")
    @Expose
    var voteAverage: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("popularity")
    @Expose
    var popularity: String? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<String>? = null

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null

    @SerializedName("adult")
    @Expose
    var adult: String? = null

    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
}
