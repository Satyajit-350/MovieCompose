package com.satyajit.moviecompose.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.satyajit.moviecompose.data.local.database.MovieDatabase
import com.satyajit.moviecompose.data.paging.SearchPagingSource
import com.satyajit.moviecompose.data.paging.MovieRemoteMediator
import com.satyajit.moviecompose.data.paging.RecommendationPagingSource
import com.satyajit.moviecompose.data.remote.MovieApi
import com.satyajit.moviecompose.modals.Movie
import com.satyajit.moviecompose.modals.MovieDetails
import com.satyajit.moviecompose.modals.MovieRemoteKeys
import com.satyajit.moviecompose.utils.Constants.ITEMS_PER_PAGE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@ExperimentalPagingApi
class MovieRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val movieAPI: MovieApi,
    private val movieDatabase: MovieDatabase
) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)

    fun getAllMovies(): Flow<PagingData<Movie>> {
        Log.d("MovieRepository", "repository_hit")
        val pagingSourceFactory = { movieDatabase.movieDao().getAllImages() }
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 2,
                initialLoadSize = 19,
                enablePlaceholders = true
            ),
            remoteMediator = MovieRemoteMediator(
                movieApi = movieAPI,
                movieDatabase = movieDatabase,
                sharedPreferences = sharedPreferences
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(movieApi = movieAPI, query = query)
            }
        ).flow
    }

    fun getRecommendations(movieId: Int): Flow<PagingData<Movie>>{
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                RecommendationPagingSource(movieApi = movieAPI, movieId = movieId)
            }
        ).flow
    }

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> get() = _movieDetails

    suspend fun getMovieDetails(movieId: Int){
        Log.d("MovieRepository", movieId.toString())
        try{
            val response = movieAPI.getDetails(movieId)
            _movieDetails.postValue(response)
        }catch (e:Exception){
            Log.d("MovieRepository", e.message.toString())
        }
    }


    suspend fun workerTaskInBackGround(){

        val currentPage = sharedPreferences.getInt("currentPage", 1)
        Log.d("currentPage1", currentPage.toString())
        val response = movieAPI.getAllMovies(page = currentPage + 1, language = "en-US")

        val endOfPaginationReached = currentPage == 501

        val prevPage = if (currentPage == 1) null else currentPage - 1
        val nextPage = if (endOfPaginationReached) null else currentPage + 1

        sharedPreferences.edit().putInt("currentPage", nextPage!!).apply()

        movieDatabase.withTransaction {

            val keys = response.results.map { movie ->
                MovieRemoteKeys(
                    movieId = movie.moveId,
                    prevPage = prevPage,
                    nextPage = nextPage
                )
            }
            //store in the database or save
            movieDatabase.movieDao().addMovies(movies = response.results)
            movieDatabase.movieRemoteKeysDao().addAllRemoteKeys(remoteKeys = keys)
        }
    }
}