package com.satyajit.moviecompose.presentation.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.satyajit.moviecompose.data.repository.MovieRepository
import com.satyajit.moviecompose.modals.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _searchedImages = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val searchedImages = _searchedImages

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            repository.searchMovies(query = query).cachedIn(viewModelScope).collect {
                _searchedImages.value = it
            }
        }
    }

}