package com.team8.moviecatalog.models.anime

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeDetail(

	@field:SerializedName("title_japanese")
	val titleJapanese: String? = null,

	@field:SerializedName("favorites")
	val favorites: Int? = null,

	@field:SerializedName("broadcast")
	val broadcast: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("scored_by")
	val scoredBy: Int? = null,

	@field:SerializedName("premiered")
	val premiered: String? = null,

	@field:SerializedName("request_cache_expiry")
	val requestCacheExpiry: Int? = null,

	@field:SerializedName("title_synonyms")
	val titleSynonyms: List<String?>? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("opening_themes")
	val openingThemes: List<String?>? = null,

	@field:SerializedName("related")
	val related: Related? = null,

	@field:SerializedName("request_hash")
	val requestHash: String? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("popularity")
	val popularity: Int? = null,

	@field:SerializedName("members")
	val members: Int? = null,

	@field:SerializedName("request_cached")
	val requestCached: Boolean? = null,

	@field:SerializedName("title_english")
	val titleEnglish: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("airing")
	val airing: Boolean? = null,

	@field:SerializedName("episodes")
	val episodes: Int? = null,

	@field:SerializedName("aired")
	val aired: Aired? = null,

	@field:SerializedName("studios")
	val studios: List<StudiosItem?>? = null,

	@field:SerializedName("ending_themes")
	val endingThemes: List<String?>? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("synopsis")
	val synopsis: String? = null,

	@field:SerializedName("licensors")
	val licensors: List<LicensorsItem?>? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("trailer_url")
	val trailerUrl: String? = null,

	@field:SerializedName("producers")
	val producers: List<Produceritem?>? = null,

	@field:SerializedName("background")
	val background: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class StudiosItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class Prop(

	@field:SerializedName("from")
	val from: From? = null,

	@field:SerializedName("to")
	val to: To? = null
) : Parcelable

@Parcelize
data class To(

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
) : Parcelable

@Parcelize
data class LicensorsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class Related(

	@field:SerializedName("Sequel")
	val sequel: List<SequelItem?>? = null,

	@field:SerializedName("Adaptation")
	val adaptation: List<AdaptationItem?>? = null
) : Parcelable

@Parcelize
data class AdaptationItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class SequelItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class From(

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
) : Parcelable

@Parcelize
data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class Aired(

	@field:SerializedName("string")
	val string: String? = null,

	@field:SerializedName("prop")
	val prop: Prop? = null,

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("to")
	val to: String? = null
) : Parcelable

@Parcelize
data class Produceritem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable
