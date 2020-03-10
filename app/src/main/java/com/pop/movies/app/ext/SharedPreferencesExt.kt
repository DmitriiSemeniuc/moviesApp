package com.pop.movies.app.ext

import android.content.SharedPreferences

fun SharedPreferences.putInt(
    key: String,
    value: Int
) {
    this.edit().putInt(key, value).apply()
}