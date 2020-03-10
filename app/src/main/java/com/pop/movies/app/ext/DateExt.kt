package com.pop.movies.app.ext

import com.pop.movies.app.constants.Const
import java.text.SimpleDateFormat
import java.util.*

fun Date.toYear(locale: Locale = Locale.getDefault()): String {
    val sdf = SimpleDateFormat(Const.YEAR_TIME_FORMAT, locale)
    // use Default as timezone
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}