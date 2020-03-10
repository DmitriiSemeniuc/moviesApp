package com.pop.movies.app.utils

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Exponential backoff that respects the equation: delay * retries ^ 2 * jitter
 */
class ObservableExpBackoff(
    private val flowName : String,
    private val retries: Int = 3,
    private val jitter: Jitter = DefaultJitter(),
    private val unit: TimeUnit = TimeUnit.MILLISECONDS
) : Function1<Observable<out Throwable>, Observable<Long>> {

    @Throws(Exception::class)
    override fun invoke(observable: Observable<out Throwable>): Observable<Long> {
        Timber.d("invoked")
        return observable
            .zipWith(Observable.range(1, retries), BiFunction<Throwable, Int, Int> { error, retryCount ->
                if(isInternetError(error) || retryCount > retries) {
                    throw error
                } else {
                    Timber.tag("Backoff").d("RETRY $retryCount for $flowName")
                    retryCount
                }
            })
            .flatMap { attemptNumber -> Observable.timer(getNewInterval(attemptNumber), unit) }
    }

    private fun getNewInterval(retryCount: Int): Long {

        var newInterval = (Math.pow(retryCount.toDouble(), 1.1) * jitter.get()) * 1000
        if (newInterval < 0) {
            newInterval = 1.0
        }
        val seconds = newInterval.toLong()
        Timber.tag("Backoff").d("Interval $seconds")
        return seconds
    }

    private fun isInternetError(it: Throwable) : Boolean {
        return (it is UnknownHostException || it is ConnectException)
    }

    interface Jitter {
        fun get(): Double
    }

    class DefaultJitter : Jitter {
        private val random = Random()

        /** Returns a random value inside [0.85, 1.15] every time it's called  */
        override fun get(): Double = 0.85 + random.nextDouble() % 0.3f
    }
}