package com.pop.movies.app.persistence.repository

import androidx.paging.DataSource
import com.pop.movies.app.models.Movie
import com.pop.movies.app.persistence.AppDatabase
import com.pop.movies.app.persistence.dao.MoviesDao
import timber.log.Timber

class MoviesDataSource(database: AppDatabase)
    : AbstractDataSource<Movie>(database.moviesDao()), MoviesRepository {

    var moviesDao: MoviesDao = dao as MoviesDao

    override fun getAll(): DataSource.Factory<Int, Movie> {
        Timber.tag(TAG).d("getAll")
        return moviesDao.getAll()
    }

    override fun getAllSync(): List<Movie>? {
        Timber.tag(TAG).d("getAllSync")
        return moviesDao.getAllSync()
    }

    override fun count(): Int {
        Timber.tag(TAG).d("count")
        return moviesDao.count()
    }

    override fun firstMovie() : Movie? {
        Timber.tag(TAG).d("firstMovie")
        return moviesDao.firstMovie()
    }

    override fun lastMovie() : Movie? {
        Timber.tag(TAG).d("lastMovie")
        return moviesDao.lastMovie()
    }

    override fun getById(id: Long) : Movie? {
        Timber.tag(TAG).d("getById")
        return moviesDao.getById(id)
    }

    override fun deleteAll() {
        Timber.tag(TAG).d("deleteAll")
        return moviesDao.deleteAll()
    }

    companion object {

        const val TAG = "MoviesDao"
    }
}