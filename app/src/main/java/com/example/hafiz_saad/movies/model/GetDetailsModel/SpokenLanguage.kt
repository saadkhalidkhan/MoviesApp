package com.example.hafiz_saad.movies.model.GetDetailsModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SpokenLanguage {
    @SerializedName("iso6391")
    @Expose
    var iso6391: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
}
