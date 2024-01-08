package com.satyajit.moviecompose.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.satyajit.moviecompose.data.local.dao.MovieDao
import com.satyajit.moviecompose.data.local.dao.MovieRemoteKeyDao
import com.satyajit.moviecompose.modals.Movie
import com.satyajit.moviecompose.modals.MovieRemoteKeys

@Database(entities = [Movie::class, MovieRemoteKeys::class], version = 22)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
}