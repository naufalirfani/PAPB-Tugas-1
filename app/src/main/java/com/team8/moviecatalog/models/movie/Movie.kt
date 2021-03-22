package com.team8.moviecatalog.models.movie

import com.google.gson.annotations.SerializedName

data class Movie(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("page")
	val page: Int? = null
)