package com.pop.movies.app.models.response

import com.pop.movies.app.models.Video

data class VideoResponse(
    val id: Long,
    val results: List<Video>?
)