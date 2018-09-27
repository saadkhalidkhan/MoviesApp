package com.example.hafiz_saad.movies.model.VideoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoResult {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("iso6391")
    @Expose
    var iso6391: String? = null
    @SerializedName("iso31661")
    @Expose
    var iso31661: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("site")
    @Expose
    var site: String? = null
    @SerializedName("size")
    @Expose
    var size: Int = 0
    @SerializedName("type")
    @Expose
    var type: String? = null
}
