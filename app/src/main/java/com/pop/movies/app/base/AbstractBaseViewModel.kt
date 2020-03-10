package com.pop.movies.app.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pop.movies.app.ext.emptySubscribe
import com.pop.movies.app.ext.io
import com.pop.movies.app.ext.toObservable
import com.pop.movies.app.utils.connection.ConnectionLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class AbstractBaseViewModel(val connectionLiveData: ConnectionLiveData? = null) :
    ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    protected val internetConnectedSubject: Subject<Unit> = PublishSubject.create()

    protected val internetConnectionState = MutableLiveData<Boolean>()
    val internetConnection: LiveData<Boolean> get() = internetConnectionState

    protected val errorState: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> get() = errorState

    var internetConnected = true

    init {
        listenForInternetConnectionUiUpdates()
        listenForInternetConnectionLiveData()
    }

    private fun listenForInternetConnectionLiveData() {
        connectionLiveData?.observeForever { online ->
            internetConnected = when {
                online -> {
                    if (!internetConnected) internetConnectedSubject.onNext(Unit)
                    true
                }
                else -> {
                    onInternetDisconnected()
                    false
                }
            }
        }
    }

    private fun listenForInternetConnectionUiUpdates() {
        addDisposable(
            internetConnectedSubject.toObservable()
                .delay(2000, TimeUnit.MILLISECONDS)
                .io()
                .doOnNext { onInternetConnected() }
                .emptySubscribe()
        )
    }

    open fun onInternetConnected() {
        Timber.d("onInternetConnected")
        internetConnectionState.postValue(true)
    }

    open fun onInternetDisconnected() {
        Timber.d("onInternetDisconnected")
        internetConnectionState.postValue(false)
    }

    open fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun <T> Observable<T>.addDisposable() = addDisposable(emptySubscribe())

    fun setError(error: String?) {
        if(error.isNullOrEmpty()) {
            errorState.value = null
        } else {
            errorState.postValue(error)
        }
    }

    @CallSuper
    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}