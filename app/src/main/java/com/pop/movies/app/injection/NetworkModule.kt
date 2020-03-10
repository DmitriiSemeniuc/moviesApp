package com.pop.movies.app.injection

import android.content.Context
import android.content.res.Resources
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.pop.movies.app.BuildConfig
import com.pop.movies.app.api.AuthorizedApi
import com.pop.movies.app.api.ErrorReader
import com.pop.movies.app.api.TokenAuthenticator
import com.pop.movies.app.constants.Const.SERVER_TIME_FORMAT
import io.reactivex.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { TokenAuthenticator() as Authenticator }
    single { createGson() }
    single { createAuthorizedApiClient(authenticator = get(), gson = get()) }
    single { ErrorReader(resources = getResources(context = get())) }
}

private fun createGson(): Converter.Factory {
    val gson = GsonBuilder().setLenient().setDateFormat(SERVER_TIME_FORMAT).create()
    return GsonConverterFactory.create(gson) as Converter.Factory
}

private fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}

private fun createAuthorizedApiClient(
    authenticator: Authenticator,
    gson: Converter.Factory
): AuthorizedApi {
    return createAuthorizedRetrofit(authenticator, gson).create(AuthorizedApi::class.java)
}

private fun createAuthorizedRetrofit(
    authenticator: Authenticator,
    gson: Converter.Factory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        )
        .addConverterFactory(gson)
        .client(createAuthorizedOkHttpClient(authenticator))
        .build()
}

private fun createAuthorizedOkHttpClient(authenticator: Authenticator): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(createHttpLoggingInterceptor())
        .authenticator(authenticator)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()
}

private fun getResources(context: Context): Resources {
    return context.resources
}
