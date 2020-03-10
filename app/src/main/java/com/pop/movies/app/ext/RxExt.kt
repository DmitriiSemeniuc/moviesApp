package com.pop.movies.app.ext

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

fun <T> Observable<T>.emptySubscribe() = subscribe({}, { it.printStackTrace() })

fun <T> Subject<T>.toObservable(strategy: BackpressureStrategy = BackpressureStrategy.BUFFER)
        = this.toFlowable(strategy).toObservable()

fun <T> Observable<T>.io(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}