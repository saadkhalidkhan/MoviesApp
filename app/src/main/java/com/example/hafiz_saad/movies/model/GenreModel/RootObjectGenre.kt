package com.example.hafiz_saad.movies.model.GenreModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RootObjectGenre {
    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null
}