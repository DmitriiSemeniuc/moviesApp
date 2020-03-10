package com.pop.movies.app.ext

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    this.startActivity(intent, options)
}

inline fun <reified  T: Any> newIntent(context: Context): Intent = Intent(context, T::class.java)

fun Context.isLandscape() : Boolean {
    return this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun Context.isTablet(): Boolean {
    return resources.configuration.smallestScreenWidthDp >= 600
}