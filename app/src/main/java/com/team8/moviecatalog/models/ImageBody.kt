package com.team8.moviecatalog.models

import com.google.gson.annotations.SerializedName

data class ImageBody (
    @SerializedName("image")
    var image: String
)
