package com.team8.moviecatalog.api.movie

import androidx.lifecycle.LiveData
import com.team8.moviecatalog.models.movie.Movie

interface MovieDataSource {

    fun getMovieNewUpload(page: Int?): LiveData<Movie>

    fun getMovieComingSoon(): LiveData<Movie>

    fun getTV(page: Int?): LiveData<Movie>

    fun getMovieByYear(year: Int?, page: Int?): LiveData<Movie>

    fun getMovieByCountry(country: String?, page: Int?): LiveData<Movie>

    fun getMovieByGenre(genre: String?, page: Int?): LiveData<Movie>

    fun getMovieBySearch(query: String?, page: Int?): LiveData<Movie>

//    fun setFavoriteMovie(movie: MovieEntity)
//
//    fun setFavoriteTvShow(tvShow: TvShowEntity)

}