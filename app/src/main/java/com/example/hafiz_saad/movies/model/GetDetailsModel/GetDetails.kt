package com.example.hafiz_saad.movies.model.GetDetailsModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetDetails {
    @SerializedName("adult")
    @Expose
    var adult: String? = null
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
    @SerializedName("belongs_to_collection")
    @Expose
    var belongsToCollection: BelongsToCollection? = null
    @SerializedName("budget")
    @Expose
    var budget: String? = null
    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("imdb_id")
    @Expose
    var imdbId: String? = null
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("popularity")
    @Expose
    var popularity: Double = 0.toDouble()
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("production_countries")
    @Expose
    var productionCountries: List<ProductionCountry>? = null
    @SerializedName("production_companies")
    @Expose
    var productionCompanies: List<ProductionCompany>? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    @SerializedName("revenue")
    @Expose
    var revenue: String? = null
    @SerializedName("runtime")
    @Expose
    var runtime: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("tagline")
    @Expose
    var tagline: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("video")
    @Expose
    var video: String? = null
    @SerializedName("spoken_languages")
    @Expose
    var spokenLanguages: List<SpokenLanguage>? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double = 0.toDouble()
    @SerializedName("vote_count")
    @Expose
    var voteCount: String? = null

}
