package com.pop.movies.app.persistence.repository

interface Repository<T> {

    fun insert(item: T)

    fun insert(items: List<T>)

    fun update(item: T)

    fun delete(item: T)

    fun deleteAll()
}