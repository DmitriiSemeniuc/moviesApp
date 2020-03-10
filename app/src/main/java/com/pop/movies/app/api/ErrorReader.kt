package com.pop.movies.app.api

import android.content.res.Resources
import android.util.MalformedJsonException
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.UnknownHostException

class ErrorReader(resources: Resources) {

        private val resources: WeakReference<Resources> = WeakReference(resources)

        fun parseError(
            rawMessage: Throwable
        ): String {
            if (resources.get() == null) {
                return "Something went wrong"
            }
            val res = resources.get()!!

            var msg = getResourceMessage(rawMessage)
            if (msg != NetworkErrorMessage.UNKNOWN) {
                return res.getString(msg)
            }
            msg = getMessageByCode(rawMessage)
            return res.getString(msg)
        }

        private fun getResourceMessage(rawMessage: Throwable): Int {
            when (rawMessage) {
                is ConnectException,
                is UnknownHostException ->
                    return NetworkErrorMessage.NO_INTERNET_CONNECTION

                is NullPointerException,
                is IllegalStateException,
                is MalformedJsonException ->
                    return NetworkErrorMessage.ERROR_IN_DATA

                is java.net.SocketException,
                is java.net.SocketTimeoutException,
                is java.io.EOFException ->
                    return NetworkErrorMessage.SERVER_PROBLEM

                is com.google.gson.JsonSyntaxException ->
                    return NetworkErrorMessage.ERROR_IN_DATA

                is javax.net.ssl.SSLPeerUnverifiedException ->
                    return NetworkErrorMessage.SSL_EXCEPTION
            }
            return NetworkErrorMessage.UNKNOWN
        }

        fun isConnectionError(throwable: Throwable): Boolean {
            return (throwable is ConnectException) ||
                    (throwable is UnknownHostException)
        }

        private fun getMessageByCode(rawMessage: Throwable): Int {

            when (rawMessage) {
                is HttpException -> {
                    return getErrorByErrorCode(rawMessage.code())
                }

                is com.jakewharton.retrofit2.adapter.rxjava2.HttpException -> {
                    return getErrorByErrorCode(rawMessage.code())
                }
            }

            return NetworkErrorMessage.UNKNOWN
        }

        private fun getErrorByErrorCode(code: Int): Int {
            return when (code) {
                400 -> NetworkErrorMessage.NOT_FOUND
                401 -> NetworkErrorMessage.NOT_AUTHORIZED
                403 -> NetworkErrorMessage.NOT_AUTHORIZED
                422 -> NetworkErrorMessage.VALIDATION_FAILED
                404 -> NetworkErrorMessage.NOT_FOUND
                429 -> NetworkErrorMessage.TRY_LATER
                500 -> NetworkErrorMessage.SERVER_PROBLEM
                else -> NetworkErrorMessage.UNKNOWN
            }
        }
}