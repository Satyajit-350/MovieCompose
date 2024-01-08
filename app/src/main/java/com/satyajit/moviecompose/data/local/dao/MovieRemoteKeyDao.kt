package com.satyajit.moviecompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.satyajit.moviecompose.modals.MovieRemoteKeys

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM movie_remote_key_table WHERE movieId =:id")
    suspend fun getRemoteKeys(id: Int): MovieRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MovieRemoteKeys>)

    @Query("DELETE FROM movie_remote_key_table")
    suspend fun deleteAllRemoteKeys()

}