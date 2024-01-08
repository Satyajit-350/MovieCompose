package com.satyajit.moviecompose.modals

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String
): Parcelable
