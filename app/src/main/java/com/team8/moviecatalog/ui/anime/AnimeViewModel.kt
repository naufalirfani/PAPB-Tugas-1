package com.team8.moviecatalog.ui.anime

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team8.moviecatalog.api.anime.AnimeClient
import com.team8.moviecatalog.api.anime.AnimeDataSource
import com.team8.moviecatalog.api.anime.SearchAnimeClient
import com.team8.moviecatalog.models.ImageBody
import com.team8.moviecatalog.models.anime.Anime
import com.team8.moviecatalog.models.anime.AnimeDetail
import com.team8.moviecatalog.models.anime.AnimeSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class AnimeViewModel : AnimeDataSource, ViewModel() {

    private val animeClient: AnimeClient = AnimeClient()
    private val searchAnimeClient: SearchAnimeClient = SearchAnimeClient()
    private var movieData =  MutableLiveData<Anime>()
    private var AnimeDataBySearch =  MutableLiveData<Anime>()
    private var animeSearch =  MutableLiveData<AnimeSearch>()
    private var animeDetail =  MutableLiveData<AnimeDetail>()

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
                    println(t.message)
                }

                override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                    if(response.code() == 200)
                        AnimeDataBySearch.value = response.body()
                    else
                        AnimeDataBySearch.value = Anime()

                }

            })
        return AnimeDataBySearch
    }

    override fun searchAnime(base64: String): LiveData<AnimeSearch> {
        searchAnimeClient.getService().getResult(ImageBody(base64))
            .enqueue(object : Callback<AnimeSearch> {
                override fun onFailure(call: Call<AnimeSearch>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(call: Call<AnimeSearch>, response: Response<AnimeSearch>) {
                    animeSearch.value = response.body()
                }

            })
        return animeSearch
    }

    override fun getByid(url: String): LiveData<AnimeDetail> {
        animeClient.getService().getById(url)
            .enqueue(object : Callback<AnimeDetail> {
                override fun onFailure(call: Call<AnimeDetail>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(call: Call<AnimeDetail>, response: Response<AnimeDetail>) {
                    animeDetail.value = response.body()

                }

            })
        return animeDetail
    }


}