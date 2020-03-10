package com.pop.movies.app.base

import android.content.Context
import android.content.Intent
import com.pop.movies.app.activities.MoviesActivity
import com.pop.movies.app.activities.SplashActivity
import com.pop.movies.app.ext.launchActivity
import timber.log.Timber

class Router(val context: Context) {

    fun showMoviesScreen() {
        Timber.tag(TAG).d("showMoviesScreen")
        context.launchActivity<MoviesActivity> {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    companion object {

        const val TAG = "Router"
    }
}