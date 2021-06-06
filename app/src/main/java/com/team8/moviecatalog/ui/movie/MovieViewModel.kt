package com.team8.moviecatalog.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team8.moviecatalog.api.movie.MovieClient
import com.team8.moviecatalog.api.movie.MovieDataSource
import com.team8.moviecatalog.models.movie.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class MovieViewModel : MovieDataSource, ViewModel() {

    private val movieClient: MovieClient = MovieClient()
    private var movieData =  MutableLiveData<Movie>()
    private var movieDataBySearch =  MutableLiveData<Movie>()

    override fun getMovieNewUpload(page: Int?): LiveData<Movie> {
        movieData.value = null
        movieClient.getService().getMovieNewUpload(page)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Logger.getLogger(t.message.toString())
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        movieData.value = response.body()
                    }

                })
        return movieData
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
        movieClient.getService().getMovieByGenre(genre, page)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Logger.getLogger(t.message.toString())
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        movieData.value = response.body()
                    }

                })
        return movieData
    }

    override fun getMovieBySearch(query: String?, page: Int?): LiveData<Movie> {
        movieClient.getService().getMovieBySearch(query, page)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Logger.getLogger(t.message.toString())
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    movieDataBySearch.value = response.body()

                }

            })
        return movieDataBySearch
    }
}