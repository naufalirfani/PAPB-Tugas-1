package com.team8.moviecatalog.database.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movie_favorite")
class MovieEntity {

	@PrimaryKey(autoGenerate = true)
	var id: Int? = null

	@ColumnInfo(name = "duration")
	var duration: String? = null

	@ColumnInfo(name = "trailer")
	var trailer: String? = null

	@ColumnInfo(name = "thumbnail")
	var thumbnail: String? = null

	@ColumnInfo(name = "watch")
	var watch: String? = null

	@ColumnInfo(name = "genre")
	var genre: String? = null

	@ColumnInfo(name = "rating")
	var rating: String? = null

	@ColumnInfo(name = "title")
	var title: String? = null

	@ColumnInfo(name = "quality")
	var quality: String? = null
}