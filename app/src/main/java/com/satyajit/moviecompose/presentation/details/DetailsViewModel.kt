package com.satyajit.moviecompose.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.satyajit.moviecompose.data.repository.MovieRepository
import com.satyajit.moviecompose.modals.Movie
import com.satyajit.moviecompose.modals.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    val movieDetails = repository.movieDetails

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
           repository.getMovieDetails(movieId)
        }
    }

    private val _movie_recommendations = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movie_recommendations = _movie_recommendations

    fun getRecommendations(movieId: Int) {
        viewModelScope.launch {
            repository.getRecommendations(movieId = movieId).cachedIn(viewModelScope).collect {
                _movie_recommendations.value = it
            }
        }
    }

}