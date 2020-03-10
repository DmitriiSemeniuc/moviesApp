package com.pop.movies.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pop.movies.app.R
import com.pop.movies.app.api.ApiClient
import com.pop.movies.app.api.ApiClient.Companion.PAGE_SIZE
import com.pop.movies.app.api.ErrorReader
import com.pop.movies.app.api.ApiClient.Companion.MAX_PAGES
import com.pop.movies.app.base.AbstractBaseViewModel
import com.pop.movies.app.base.App
import com.pop.movies.app.ext.io
import com.pop.movies.app.models.Movie
import com.pop.movies.app.persistence.repository.MoviesRepository
import com.pop.movies.app.viewmodels.MoviesActivityViewModel.Companion.selectedMovieLiveData
import io.reactivex.Observable
import timber.log.Timber

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository,
    private val apiClient: ApiClient,
    private val errorReader: ErrorReader
) : AbstractBaseViewModel() {

    lateinit var repositoryLiveData: LiveData<PagedList<Movie>>

    private val moviesState: MutableLiveData<PagedList<Movie>> = MutableLiveData()
    val movies: LiveData<PagedList<Movie>> get() = moviesState

    private val firstMovieEventState: MutableLiveData<Movie?> = MutableLiveData()
    val firstMovieEvent: LiveData<Movie?> get() = firstMovieEventState

    init {
        Timber.tag(TAG).d("init: ")
        setupRepositoryListener()
        checkSelectedMovie()
    }

    private fun setupRepositoryListener() {
        Timber.tag(TAG).d("setupRepositoryListener: ")
        val liveData = moviesRepository.getAll()
        repositoryLiveData = LivePagedListBuilder(liveData, getPageListConfig())
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Movie>() {
                override fun onZeroItemsLoaded() {
                    loadData()
                }

                override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                    loadData()
                }
            })
            .build()

        repositoryLiveData.observeForever { moviesState.postValue(it) }
    }

    private fun checkSelectedMovie() {
        Timber.tag(TAG).d("checkSelectedMovie: ")
        if(selectedMovieLiveData.value == null) {
            Observable.just(moviesRepository)
                .map { moviesRepository.firstMovie() }
                .filter { it != null }
                .io()
                .doOnNext { selectedMovieLiveData.postValue(it) }
                .doOnError { Timber.e(it) }
                .addDisposable()
        }
    }

    private fun getPageListConfig(): PagedList.Config {
        Timber.tag(TAG).d("getPageListConfig: ")
        return PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(20)
            .setPrefetchDistance(60)
            .setEnablePlaceholders(true)
            .build()
    }

    private fun loadData() {
        Timber.tag(TAG).d("loadData: ")
        if (isLoading) return
        performDataLoading()
    }

    fun reloadData() {
        Timber.tag(TAG).d("reloadData: ")

        isFreshData = true
        if (!internetConnected) {
            setError(App.getAppContext().getString(R.string.verify_internet_connection))
        }

        Observable.just(1)
            .flatMap { fetchAndSaveMovies(it) }
            .io()
            .doOnError { handleError(it) }
            .addDisposable()
    }

    private fun performDataLoading() {
        Timber.tag(TAG).d("performDataLoading: ")
        isLoading = true
        if (!internetConnected) {
            setError(App.getAppContext().getString(R.string.verify_internet_connection))
        }

        Observable.just("")
            .map { getNextPage() }
            .flatMap { fetchAndSaveMovies(it) }
            .doOnNext { movies ->
                if (!movies.isNullOrEmpty()) {
                    notifyFirstMovieLoaded(movies.first())
                }
            }
            .io()
            .doOnError {
                isLoading = false
                handleError(it)
            }
            .addDisposable()
    }

    private fun fetchAndSaveMovies(page: Int): Observable<List<Movie>> {
        Timber.tag(TAG).d("fetchAndSaveMovies: $page")
        return Observable.just(page)
            .filter { it != -1 }
            .flatMap { page ->
                apiClient.getPopMovies(page).map {
                    return@map Pair(it, page)
                }
            }
            .map { applyOrder(it.first.results.toMutableList(), it.second) }
            .doOnNext { movies ->
                /*if(isFreshData) {
                    moviesRepository.deleteAll()
                    isFreshData = false
                }*/
                moviesRepository.insert(movies)
                isLoading = false
            }
    }

    private fun getNextPage(): Int {
        Timber.tag(TAG).d("getNextPage: ")
        var lastPage = moviesRepository.lastMovie()?.page
        if (lastPage == null) {
            lastPage = 0
        }
        val nextPage = lastPage + 1
        return when {
            nextPage >= MAX_PAGES -> -1
            else -> nextPage
        }
    }

    private fun applyOrder(movies: MutableList<Movie>, page: Int): List<Movie> {
        Timber.tag(TAG).d("applyOrder: ")
        var count = 1
        movies.forEach {
            val c = if (count < 10) "${0}$count" else "$count"
            val order = "$page$c"
            it.computeOrder = order.toLong()
            it.page = page
            count += 1
        }
        return movies
    }

    private fun notifyFirstMovieLoaded(movie: Movie) {
        Timber.tag(TAG).d("notifyFirstMovieLoaded: $movie")
        if (selectedMovieLiveData.value == null) {
            selectedMovieLiveData.postValue(movie)
            firstMovieEventState.postValue(movie)
        }
    }

    override fun onInternetConnected() {
        super.onInternetConnected()
        loadDataIfEmpty()
    }

    private fun loadDataIfEmpty() {
        Timber.tag(TAG).d("loadDataIfEmpty: ")
        Observable.just(moviesRepository)
            .map { moviesRepository.count() <= 0 }
            .filter { it == true }
            .io()
            .doOnNext { loadData() }
            .doOnError { Timber.e(it) }
            .addDisposable()
    }

    private fun handleError(throwable: Throwable) {
        Timber.tag(TAG).d("handleError: ${throwable.message}")
        val error = errorReader.parseError(throwable)
        setError(error)
        Timber.e(error)
    }

    companion object {

        const val TAG = "MoviesVM"

        @Volatile
        private var isLoading = false

        @Volatile
        private var isFreshData = true
    }
}