package com.team8.moviecatalog.api.anime

import androidx.lifecycle.LiveData
import com.team8.moviecatalog.models.anime.Anime

interface AnimeDataSource {
    fun getAnimeDetail(id: Int?): LiveData<Anime>
    fun getAnimeBySearch(q: String?, orderBy: String?, sort: String?, page: Int?): LiveData<Anime>
}