package com.team8.moviecatalog.api.anime

import com.team8.moviecatalog.models.ImageBody
import com.team8.moviecatalog.models.anime.AnimeSearch
import retrofit2.Call
import retrofit2.http.*

interface SearchAnimeService {

    @POST("/api/search")
    fun getResult(@Body image: ImageBody): Call<AnimeSearch>
}