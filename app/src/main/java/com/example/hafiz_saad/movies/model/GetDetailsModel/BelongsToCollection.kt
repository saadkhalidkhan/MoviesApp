package com.example.hafiz_saad.movies.model.GetDetailsModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BelongsToCollection {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("posterPath")
    @Expose
    var posterPath: String? = null
    @SerializedName("backdropPath")
    @Expose
    var backdropPath: String? = null
}
