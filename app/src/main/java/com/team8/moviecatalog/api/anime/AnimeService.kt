package com.team8.moviecatalog.api.anime

import com.team8.moviecatalog.models.anime.Anime
import retrofit2.Call
import retrofit2.http.*

interface AnimeService {

    @GET("/v3/genre")
    fun getAnimeDetail(@Query("id") id: Int?): Call<Anime>

    @GET("/v3/search/anime")
    fun getAnimeBySearch(@Query("q") q: String?,
                         @Query("order_by") orderBy: String?,
                         @Query("sort") sort: String?,
                         @Query("page") page: Int?): Call<Anime>
}