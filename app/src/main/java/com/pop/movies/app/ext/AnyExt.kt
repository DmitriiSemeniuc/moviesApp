package com.pop.movies.app.ext

import android.os.Handler

fun delayedAction(delay: Long, action: () -> Unit) {
    Handler().postDelayed({
        action()
    }, delay)
}