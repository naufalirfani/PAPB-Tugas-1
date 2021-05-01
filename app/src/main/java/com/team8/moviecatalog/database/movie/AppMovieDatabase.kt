package com.team8.moviecatalog.database.movie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class], version = 1)
abstract class AppMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "favorite"
        private var INSTANCE: AppMovieDatabase? = null
        fun getInstance(context: Context?): AppMovieDatabase? {
            if (INSTANCE == null) {
                INSTANCE = context?.let {
                    Room
                        .databaseBuilder(
                            it,
                            AppMovieDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                }
            }
            return INSTANCE
        }
    }
}