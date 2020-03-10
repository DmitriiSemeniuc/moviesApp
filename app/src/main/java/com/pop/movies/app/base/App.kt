package com.pop.movies.app.base

import android.app.Application
import com.pop.movies.app.BuildConfig
import com.pop.movies.app.injection.appModules
import com.pop.movies.app.utils.DebugLogTree
import com.pop.movies.app.utils.ReleaseLogTree
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.io.IOException
import java.net.SocketException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
        setupLogTree()
        setupRxJavaPlugins()
    }

    private fun setupLogTree() {
        when {
            BuildConfig.BUILD_TYPE.contentEquals("release") -> Timber.plant(ReleaseLogTree())
            else -> Timber.plant(DebugLogTree())
        }
    }

    private fun setupRxJavaPlugins() {
        RxJavaPlugins.setErrorHandler { error ->
            if (error is UndeliverableException) {
                Timber.tag("App").e(error.cause)
            }
            if ((error is IOException) || (error is SocketException)) {
                // irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if ((error is InterruptedException)) {
                // some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
        }
    }

    companion object {
        private var instance: App? = null
        fun getAppContext() = instance as App
    }
}