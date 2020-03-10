package com.pop.movies.app.ext

import com.pop.movies.app.constants.Const
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(dateFormat: String = Const.SERVER_TIME_FORMAT, timeZone: TimeZone = TimeZone.getDefault()): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}