package com.pop.movies.app.models.response

data class BasePaginationResponse<T>(
    val error: Error? = null,
    val data: BasePagination<T>
)