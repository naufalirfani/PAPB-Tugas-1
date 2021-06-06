package com.team8.moviecatalog.models.anime

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeSearch(

	@field:SerializedName("RawDocsCount")
	val rawDocsCount: Int? = null,

	@field:SerializedName("CacheHit")
	val cacheHit: Boolean? = null,

	@field:SerializedName("docs")
	val docs: List<DocsItem?>? = null,

	@field:SerializedName("limit_ttl")
	val limitTtl: Int? = null,

	@field:SerializedName("RawDocsSearchTime")
	val rawDocsSearchTime: Int? = null,

	@field:SerializedName("quota")
	val quota: Int? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("ReRankSearchTime")
	val reRankSearchTime: Int? = null,

	@field:SerializedName("quota_ttl")
	val quotaTtl: Int? = null,

	@field:SerializedName("trial")
	val trial: Int? = null
) : Parcelable

@Parcelize
data class DocsItem(

	@field:SerializedName("title_chinese")
	val titleChinese: String? = null,

	@field:SerializedName("title_native")
	val titleNative: String? = null,

	@field:SerializedName("synonyms")
	val synonyms: List<String?>? = null,

	@field:SerializedName("title_romaji")
	val titleRomaji: String? = null,

	@field:SerializedName("episode")
	val episode: Int? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("anilist_id")
	val anilistId: Int? = null,

	@field:SerializedName("is_adult")
	val isAdult: Boolean? = null,

	@field:SerializedName("synonyms_chinese")
	val synonymsChinese: List<String?>? = null,

	@field:SerializedName("tokenthumb")
	val tokenthumb: String? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("at")
	val at: Double? = null,

	@field:SerializedName("similarity")
	val similarity: Double? = null,

	@field:SerializedName("season")
	val season: String? = null,

	@field:SerializedName("title_english")
	val titleEnglish: String? = null,

	@field:SerializedName("from")
	val from: Double? = null,

	@field:SerializedName("to")
	val to: Double? = null,

	@field:SerializedName("anime")
	val anime: String? = null
) : Parcelable
