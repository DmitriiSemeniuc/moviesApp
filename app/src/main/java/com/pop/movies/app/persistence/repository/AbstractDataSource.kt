package com.pop.movies.app.persistence.repository

import com.pop.movies.app.persistence.dao.AbstractDao

abstract class AbstractDataSource<T>(val dao: AbstractDao<T>) {

    fun insert(item: T) {
        dao.insert(item)
    }

    fun insert(items: List<T>) {
        dao.insert(items)
    }

    fun update(item: T) {
        dao.update(item)
    }

    fun delete(item: T) {
        dao.delete(item)
    }
}