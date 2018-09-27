package com.example.hafiz_saad.movies.model.NowPlayingModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class RootObject {
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
        private set

    @SerializedName("page")
    @Expose
    var page: String? = null

    @SerializedName("total_results")
    @Expose
    var totalResults: String? = null

    @SerializedName("dates")
    @Expose
    var dates: Dates? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: String? = null

    fun setResults(results: ArrayList<Result>) {
        this.results = results
    }
}