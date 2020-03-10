package com.pop.movies.app.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pop.movies.app.base.AbstractBaseViewModel
import com.pop.movies.app.common.SingleLiveEvent
import com.pop.movies.app.ext.findFragmentByTag
import com.pop.movies.app.ext.isLandscape
import com.pop.movies.app.ext.isTablet
import com.pop.movies.app.ext.removeFragment
import com.pop.movies.app.fragments.EmptyFragment
import com.pop.movies.app.models.Movie
import com.pop.movies.app.utils.connection.ConnectionLiveData
import timber.log.Timber

class MoviesActivityViewModel(context: Context, connectionLiveData: ConnectionLiveData) :
    AbstractBaseViewModel(connectionLiveData) {

    private val movieState: MutableLiveData<Movie> = SingleLiveEvent()
    val movie: LiveData<Movie> get() = movieState

    private var selectedMovieObserver = Observer<Movie?> {
        it?.let {
            movieState.postValue(it)
        }
    }

    init {
        Timber.tag(TAG).d("init: ")
        if (context.isTablet() && context.isLandscape()) {
            if (selectedMovieLiveData.value == null) {
                selectedMovieLiveData.observeForever(selectedMovieObserver)
            }
        }
        movie.observeForever {
            selectedMovieObserver.let {
                selectedMovieLiveData.removeObserver(selectedMovieObserver)
            }
        }
    }

    companion object {
        const val TAG = "MoviesActivityVM"

        @Volatile
        var selectedMovieLiveData: MutableLiveData<Movie?> = MutableLiveData(null)
    }
}