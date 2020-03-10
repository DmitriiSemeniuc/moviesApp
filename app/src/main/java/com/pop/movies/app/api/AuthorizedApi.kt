package com.pop.movies.app.api

import com.pop.movies.app.models.Movie
import com.pop.movies.app.models.response.BasePagination
import com.pop.movies.app.models.response.VideoResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthorizedApi {

    @GET("/3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<BasePagination<List<Movie>>>

    @GET("/3/movie/{id}/videos")
    fun getVideos(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<VideoResponse>
}