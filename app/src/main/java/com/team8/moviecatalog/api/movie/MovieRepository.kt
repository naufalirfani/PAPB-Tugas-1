package com.team8.moviecatalog.api.movie

import androidx.lifecycle.LiveData
import com.team8.moviecatalog.models.movie.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieClient: MovieClient) : MovieDataSource {
    override fun getMovieNewUpload(page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMovieComingSoon(): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getTV(page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMovieByYear(year: Int?, page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMovieByCountry(country: String?, page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMovieByGenre(genre: String?, page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMovieBySearch(query: String?, page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }
}