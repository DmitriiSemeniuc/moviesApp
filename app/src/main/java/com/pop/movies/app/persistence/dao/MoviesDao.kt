package com.pop.movies.app.persistence.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.pop.movies.app.models.Movie

@Dao
interface MoviesDao : AbstractDao<Movie> {

    @Query("SELECT * FROM Movie ORDER BY computeOrder ASC")
    fun getAll(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM Movie ORDER BY computeOrder ASC")
    fun getAllSync(): List<Movie>?

    @Query("SELECT COUNT(id) FROM Movie")
    fun count() : Int

    @Query("SELECT * FROM Movie ORDER BY computeOrder ASC LIMIT 1")
    fun firstMovie() : Movie?

    @Query("SELECT * FROM Movie ORDER BY computeOrder DESC LIMIT 1")
    fun lastMovie() : Movie?

    @Query("DELETE FROM Movie")
    fun deleteAll()

    @Query("SELECT * FROM Movie WHERE id like :id LIMIT 1")
    fun getById(id: Long) : Movie?
}