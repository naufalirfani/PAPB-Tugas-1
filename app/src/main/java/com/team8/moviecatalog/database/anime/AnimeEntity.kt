package com.team8.moviecatalog.database.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "anime_favorite")
class AnimeEntity {

	@PrimaryKey(autoGenerate = true)
	var id: Int? = null

	@ColumnInfo(name = "end_date")
	var endDate: String? = null

	@ColumnInfo(name = "image_url")
	var imageUrl: String? = null

	@ColumnInfo(name = "mal_id")
	var malId: Int? = null

	@ColumnInfo(name = "synopsis")
	var synopsis: String? = null

	@ColumnInfo(name = "title")
	var title: String? = null

	@ColumnInfo(name = "type")
	var type: String? = null

	@ColumnInfo(name = "url")
	var url: String? = null

	@ColumnInfo(name = "rated")
	var rated: String? = null

	@ColumnInfo(name = "score")
	var score: Double? = null

	@ColumnInfo(name = "members")
	var members: Int? = null

	@ColumnInfo(name = "airing")
	var airing: Boolean? = null

	@ColumnInfo(name = "episodes")
	var episodes: Int? = null

	@ColumnInfo(name = "start_date")
	var startDate: String? = null
}