package com.team8.moviecatalog.database.anime

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime_favorite")
    fun getAll(): LiveData<List<AnimeEntity>>

    @Query("SELECT * FROM anime_favorite")
    fun getAllAnime(): Cursor
    
    @Query("SELECT * FROM anime_favorite WHERE title == :title")
    fun loadById(title: String): LiveData<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnime(anime: AnimeEntity): Long

    @Query("DELETE FROM anime_favorite")
    fun deleteAll()

    @Query("DELETE FROM anime_favorite WHERE title = :title ")
    fun deleteAnime(title: String): Int
}