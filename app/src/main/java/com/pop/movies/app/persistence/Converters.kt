package com.pop.movies.app.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pop.movies.app.models.Movie

object Converters {

    @TypeConverter
    @JvmStatic
    fun toMovie(value: String) : Movie? {
        return Gson().fromJson<Movie>(value, Movie::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun fromMovie(movie: Movie) : String {
        return Gson().toJson(movie)
    }

    @TypeConverter
    @JvmStatic
    fun toListOfLong(data: String?): List<Long> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson<List<Long>>(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromListOfLong(objects: List<Long>): String {
        return Gson().toJson(objects)
    }
}