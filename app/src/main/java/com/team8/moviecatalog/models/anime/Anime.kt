package com.team8.moviecatalog.models.anime

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Anime(

	@field:SerializedName("request_hash")
	val requestHash: String? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("request_cached")
	val requestCached: Boolean? = null,

	@field:SerializedName("request_cache_expiry")
	val requestCacheExpiry: Int? = null,

	@field:SerializedName("results")
	val results: List<AnimeResult?>? = null
) : Parcelable