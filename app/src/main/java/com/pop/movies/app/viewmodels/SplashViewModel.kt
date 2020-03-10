package com.pop.movies.app.viewmodels

import com.pop.movies.app.base.AbstractBaseViewModel
import com.pop.movies.app.base.Router
import com.pop.movies.app.ext.io
import com.pop.movies.app.utils.connection.ConnectionLiveData
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashViewModel(connectionLiveData: ConnectionLiveData, val router: Router) :
    AbstractBaseViewModel(connectionLiveData) {

    fun goToMoviesScreenDelayed() {
        Timber.tag(TAG).d("goToMoviesScreen: ")
        Observable.just(router)
            .delay(2, TimeUnit.SECONDS)
            .io()
            .doOnNext { goToMoviesScreen() }
            .doOnError { Timber.e(it) }
            .addDisposable()
    }

    private fun goToMoviesScreen() = router.showMoviesScreen()

    companion object {

        const val TAG = "SplashVM"
    }
}