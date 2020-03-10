package com.pop.movies.app.utils

import android.util.Log
import timber.log.Timber

class ReleaseLogTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return when (priority) {
            Log.ERROR -> true
            else -> false
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            super.log(priority, tag, message, t)
        }
    }
}