package com.example.hafiz_saad.movies.model.GetDetailsModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductionCompany {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("logoPath")
    @Expose
    var logoPath: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("originCountry")
    @Expose
    var originCountry: String? = null
}
