package com.pop.movies.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pop.movies.app.api.ErrorReader
import com.pop.movies.app.api.ApiClient
import com.pop.movies.app.base.AbstractBaseViewModel
import com.pop.movies.app.ext.io
import com.pop.movies.app.models.Movie
import com.pop.movies.app.models.Video
import com.pop.movies.app.viewmodels.MoviesActivityViewModel.Companion.selectedMovieLiveData
import timber.log.Timber

class MoviesDetailsViewModel(
    private val apiClient: ApiClient,
    private val errorReader: ErrorReader
) : AbstractBaseViewModel() {

    private val movieState: MutableLiveData<Movie?> = MutableLiveData()
    val movie: LiveData<Movie?> get() = movieState

    private val videosState: MutableLiveData<List<Video>?> = MutableLiveData()
    val videos: LiveData<List<Video>?> get() = videosState

    init {
        Timber.tag(TAG).d("init: ")
        listenForMovieSelection()
    }

    private fun listenForMovieSelection() {
        Timber.tag(TAG).d("listenForMovieSelection: ")
        selectedMovieLiveData.observeForever {
            movieState.postValue(it)
            it?.let {
                fetchMovieVideos(it.id)
            }
        }
    }

    private fun fetchMovieVideos(id: Long) {
        Timber.tag(TAG).d("fetchMovieVideos: ")
        videosState.value = null
        apiClient.getVideos(id)
            .io()
            .doOnNext { videosState.postValue(it.results) }
            .doOnError { handleError(it) }
            .addDisposable()
    }

    private fun handleError(throwable: Throwable) {
        Timber.tag(TAG).d("handleError: ${throwable.message}")
        val error = errorReader.parseError(throwable)
        setError(error)
        Timber.e(error)
    }

    companion object {

        const val TAG = "MoviesDetailsVM"
    }
}