package com.team8.moviecatalog.models.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultItem(

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("trailer")
	val trailer: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("watch")
	val watch: String? = null,

	@field:SerializedName("genre")
	val genre: List<String?>? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("quality")
	val quality: String? = null
):Parcelable