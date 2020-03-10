package com.pop.movies.app.models.response

import com.google.gson.annotations.SerializedName

data class BasePagination<T>(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: T
)