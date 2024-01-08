package com.satyajit.moviecompose.data.remote

import com.satyajit.moviecompose.BuildConfig
import com.satyajit.moviecompose.modals.MovieDetails
import com.satyajit.moviecompose.modals.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface  MovieApi {


    @GET("trending/movie/day")
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") api_Key: String = BuildConfig.API_KEY
    ) : MovieResponse


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") api_Key: String = BuildConfig.API_KEY
    ) : MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_Key: String = BuildConfig.API_KEY
    ):MovieDetails

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_Key: String = BuildConfig.API_KEY
    ): MovieResponse

}