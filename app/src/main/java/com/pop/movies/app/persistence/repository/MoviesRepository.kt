package com.pop.movies.app.persistence.repository

import androidx.paging.DataSource
import com.pop.movies.app.models.Movie

interface MoviesRepository : Repository<Movie> {

    fun count() : Int

    fun firstMovie() : Movie?

    fun lastMovie() : Movie?

    fun getAll(): DataSource.Factory<Int, Movie>

    fun getAllSync() : List<Movie>?

    fun getById(id: Long) : Movie?
}