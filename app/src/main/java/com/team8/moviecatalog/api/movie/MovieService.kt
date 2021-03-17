package com.team8.moviecatalog.api.movie

import retrofit2.Call
import retrofit2.http.*
import com.team8.moviecatalog.models.movie.Movie

interface MovieService {

    @GET("/newupload")
    fun getMovieNewUpload(@Query("page") page: Int?): Call<Movie>

    @GET("/comingsoon")
    fun getMovieComingSoon(): Call<Movie>

    @GET("/tv")
    fun getTV(@Query("page") page: Int?): Call<Movie>

    @GET("/year")
    fun getMovieByYear(@Query("year") year: Int?, @Query("page") page: Int?): Call<Movie>

    @GET("/country")
    fun getMovieByCountry(@Query("country") country: String?, @Query("page") page: Int?): Call<Movie>

    @GET("/genre")
    fun getMovieByGenre(@Query("genre") genre: String?, @Query("page") page: Int?): Call<Movie>

    @GET("/search")
    fun getMovieBySearch(@Query("query") query: String?, @Query("page") page: Int?): Call<Movie>
}