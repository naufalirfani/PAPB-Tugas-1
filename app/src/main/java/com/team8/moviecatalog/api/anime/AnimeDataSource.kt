package com.team8.moviecatalog.api.anime

import androidx.lifecycle.LiveData
import com.team8.moviecatalog.models.ImageBody
import com.team8.moviecatalog.models.anime.Anime
import com.team8.moviecatalog.models.anime.AnimeDetail
import com.team8.moviecatalog.models.anime.AnimeSearch

interface AnimeDataSource {
    fun getAnimeDetail(id: Int?): LiveData<Anime>
    fun getAnimeBySearch(q: String?, orderBy: String?, sort: String?, page: Int?): LiveData<Anime>
    fun searchAnime(base64: String): LiveData<AnimeSearch>
    fun getByid(url: String): LiveData<AnimeDetail>
}