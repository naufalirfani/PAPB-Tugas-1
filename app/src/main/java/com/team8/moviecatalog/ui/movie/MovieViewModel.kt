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

class MovieViewModel : MovieDataSource, ViewModel() {

    private val movieClient: MovieClient = MovieClient()
    private var movieData =  MutableLiveData<Movie>()

    override fun getMovieNewUpload(page: Int?): LiveData<Movie> {
        movieClient.getService().getMovieNewUpload(page)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
//                        showErrorToast()
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
        TODO("Not yet implemented")
    }

    override fun getMovieBySearch(query: String?, page: Int?): LiveData<Movie> {
        TODO("Not yet implemented")
    }

//    private fun showErrorToast(){
//        Toast.makeText(getApplication(), context.getString(R.string.connection_failure), Toast.LENGTH_SHORT).show()
//    }
}