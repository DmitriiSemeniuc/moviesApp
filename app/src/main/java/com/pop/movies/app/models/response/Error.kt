package com.pop.movies.app.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Error(
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status_code")
    val statusCode: Int
) : Parcelable