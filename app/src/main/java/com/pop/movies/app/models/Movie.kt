package com.pop.movies.app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pop.movies.app.api.ApiClient
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Movie")
@Parcelize
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val popularity: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    val video: Boolean?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Long>?,
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: String?,
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    var computeOrder: Long = 0,
    var page: Int = 0
) : Parcelable {

    fun getFullPosterPath() : String {
        return ApiClient.POSTER_URL + posterPath
    }

    fun getFullPosterPath300() : String {
        return ApiClient.POSTER_SIZE_300 + posterPath
    }

    fun getBackDropPath() : String {
        return ApiClient.THUMBNAIL_URL + backdropPath
    }
}