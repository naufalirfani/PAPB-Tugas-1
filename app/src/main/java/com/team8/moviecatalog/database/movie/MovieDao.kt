package com.team8.moviecatalog.database.movie

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_favorite")
    fun getAll(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movie_favorite")
    fun getAlMovie(): Cursor
    
    @Query("SELECT * FROM movie_favorite WHERE title == :title")
    fun loadById(title: String): LiveData<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieEntity): Long

    @Query("DELETE FROM movie_favorite")
    fun deleteAll()

    @Query("DELETE FROM movie_favorite WHERE title = :title ")
    fun deleteMovie(title: String): Int
}