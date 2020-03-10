package com.pop.movies.app.api

import androidx.annotation.IntDef
import com.pop.movies.app.R

@IntDef(
    NetworkErrorMessage.NO_INTERNET_CONNECTION,
    NetworkErrorMessage.SERVER_PROBLEM,
    NetworkErrorMessage.NOT_FOUND,
    NetworkErrorMessage.NOT_AUTHORIZED,
    NetworkErrorMessage.NO_SERVER_CONNECTION,
    NetworkErrorMessage.ERROR_IN_DATA,
    NetworkErrorMessage.UNKNOWN,
    NetworkErrorMessage.VALIDATION_FAILED,
    NetworkErrorMessage.SSL_EXCEPTION,
    NetworkErrorMessage.TRY_LATER
)
@Retention(AnnotationRetention.SOURCE)
annotation class NetworkErrorMessage {
    companion object {
        const val NO_INTERNET_CONNECTION = R.string.verify_internet_connection
        const val SERVER_PROBLEM = R.string.try_again_later
        const val NOT_FOUND = R.string.not_found
        const val NOT_AUTHORIZED = R.string.not_authorized
        const val NO_SERVER_CONNECTION = R.string.no_server_connection
        const val ERROR_IN_DATA = R.string.error_in_data
        const val UNKNOWN = R.string.error_unknown
        const val VALIDATION_FAILED = R.string.error_validation
        const val SSL_EXCEPTION = R.string.error_ssl
        const val TRY_LATER = R.string.please_try_again_later
    }
}