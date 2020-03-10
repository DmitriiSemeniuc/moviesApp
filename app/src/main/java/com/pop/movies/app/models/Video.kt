package com.pop.movies.app.models

import com.pop.movies.app.api.ApiClient

data class Video(
    val id: String,
    val iso_639_1: String,
    val iso_3166_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
) {

    fun getVideLink() : String {
        return ApiClient.BASE_URL_YOUTUBE + key
    }
}