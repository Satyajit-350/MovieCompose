package com.satyajit.moviecompose.modals

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.satyajit.moviecompose.utils.Constants.MOVIE_REMOTE_KEY_TABLE
import kotlinx.parcelize.Parcelize

/**
 * The main purpose of this table is store the previous and next page keys in our local database
 * so that our Remote Mediator will know which page to request next
 */
@Parcelize
@Entity(tableName = MOVIE_REMOTE_KEY_TABLE)
data class MovieRemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int,
    val prevPage: Int?,
    val nextPage: Int?
):Parcelable
