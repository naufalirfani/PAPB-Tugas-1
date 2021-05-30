package com.team8.moviecatalog.ui.anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team8.moviecatalog.api.anime.AnimeClient
import com.team8.moviecatalog.api.anime.AnimeDataSource
import com.team8.moviecatalog.models.anime.Anime
import com.team8.moviecatalog.models.movie.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class AnimeViewModel : AnimeDataSource, ViewModel() {

    private val animeClient: AnimeClient = AnimeClient()
    private var movieData =  MutableLiveData<Anime>()
    private var AnimeDataBySearch =  MutableLiveData<Anime>()

    override fun getAnimeDetail(id: Int?): LiveData<Anime> {
        TODO("Not yet implemented")
    }

    override fun getAnimeBySearch(
        q: String?,
        orderBy: String?,
        sort: String?,
        page: Int?
    ): LiveData<Anime> {
        animeClient.getService().getAnimeBySearch(q, orderBy, sort, page)
            .enqueue(object : Callback<Anime> {
                override fun onFailure(call: Call<Anime>, t: Throwable) {
                    Logger.getLogger(t.message.toString())
                }

                override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                    AnimeDataBySearch.value = response.body()

                }

            })
        return AnimeDataBySearch
    }
}