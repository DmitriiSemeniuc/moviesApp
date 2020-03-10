package com.pop.movies.app.injection

import com.pop.movies.app.api.ApiClient
import com.pop.movies.app.base.Router
import com.pop.movies.app.persistence.AppDatabase
import com.pop.movies.app.persistence.repository.MoviesDataSource
import com.pop.movies.app.persistence.repository.MoviesRepository
import com.pop.movies.app.utils.connection.ConnectionLiveData
import com.pop.movies.app.viewmodels.MoviesActivityViewModel
import com.pop.movies.app.viewmodels.MoviesDetailsViewModel
import com.pop.movies.app.viewmodels.MoviesListViewModel
import com.pop.movies.app.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ConnectionLiveData(context = get()) }
    single { Router(context = get()) }
}

val dataSourceModule = module {
    single { ApiClient(authorizedApi = get()) }
    single { AppDatabase.getInstance(context = get()) }
    single { MoviesDataSource(database = get()) as MoviesRepository }
}

val splashModule = module {
    viewModel { SplashViewModel(connectionLiveData = get(), router = get()) }
}

val moviesModule = module {
    factory { MoviesActivityViewModel(connectionLiveData = get(), context = get()) }
    viewModel {
        MoviesListViewModel(
            moviesRepository = get(),
            apiClient = get(),
            errorReader = get()
        )
    }
}

val moviesDetailsModule = module {
    viewModel {
        MoviesDetailsViewModel(apiClient = get(), errorReader = get())
    }
}

val appModules = listOf(
    networkModule,
    appModule,
    dataSourceModule,
    splashModule,
    moviesModule,
    moviesDetailsModule
)