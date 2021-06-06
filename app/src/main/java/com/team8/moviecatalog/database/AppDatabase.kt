package com.team8.moviecatalog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.team8.moviecatalog.database.anime.AnimeDao
import com.team8.moviecatalog.database.anime.AnimeEntity
import com.team8.moviecatalog.database.movie.MovieDao
import com.team8.moviecatalog.database.movie.MovieEntity


@Database(entities = [MovieEntity::class, AnimeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun animeDao(): AnimeDao

    companion object {
        private const val DATABASE_NAME = "favorite"
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context?): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = context?.let {
                    Room
                        .databaseBuilder(
                            it,
                            AppDatabase::class.java,
                                DATABASE_NAME
                        ).build()
                }
            }
            return INSTANCE
        }
    }
}