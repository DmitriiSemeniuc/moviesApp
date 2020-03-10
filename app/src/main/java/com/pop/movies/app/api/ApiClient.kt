package com.pop.movies.app.api

import com.pop.movies.app.constants.Const
import com.pop.movies.app.models.Movie
import com.pop.movies.app.models.response.BasePagination
import com.pop.movies.app.models.response.VideoResponse
import com.pop.movies.app.utils.ObservableExpBackoff
import io.reactivex.Observable
import timber.log.Timber

class ApiClient(private val authorizedApi: AuthorizedApi) {

    fun getPopMovies(page: Int): Observable<BasePagination<List<Movie>>> {
        Timber.tag(TAG).d("getPopMovies")
        return authorizedApi.getPopularMovies(
            apiKey = API_KEY,
            language = Const.APP_LANG,
            page = page
        ).retryWhen(ObservableExpBackoff("getPopMovies", 2))
    }

    fun getVideos(id: Long) : Observable<VideoResponse> {
        return authorizedApi.getVideos(
            id = id,
            apiKey = API_KEY,
            language = Const.APP_LANG
        ).retryWhen(ObservableExpBackoff("getVideos", 2))
    }

    companion object {

        const val TAG = "RestClient"
        const val PAGE_SIZE = 20
        const val MAX_PAGES = 500

        const val BASE_URL_YOUTUBE="https://www.youtube.com/watch?v="
        const val API_KEY="a998316ea58d2658720b5e81f5607883"
        const val THUMBNAIL_URL="https://image.tmdb.org/t/p/w500"
        const val POSTER_URL="https://image.tmdb.org/t/p/w342"
        const val POSTER_SIZE_300 = "https://image.tmdb.org/t/p/w185"
    }
}