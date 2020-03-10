package com.pop.movies.app.ext

import android.view.View

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.isVisible() : Boolean {
    return this.visibility == View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}