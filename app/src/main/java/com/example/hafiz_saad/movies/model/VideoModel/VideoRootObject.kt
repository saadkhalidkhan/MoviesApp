package com.example.hafiz_saad.movies.model.VideoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class VideoRootObject {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("results")
    @Expose
    var results: ArrayList<VideoResult>? = null
}
