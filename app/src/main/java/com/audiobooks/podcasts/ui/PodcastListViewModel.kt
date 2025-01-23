package com.audiobooks.podcasts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.usecase.PodcastUseCases
import com.audiobooks.podcasts.utils.ResultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastListViewModel @Inject constructor(
    private val podcastUseCases: PodcastUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ResultResponse<List<Podcast>>>(ResultResponse.Loading)
    val uiState: StateFlow<ResultResponse<List<Podcast>>> = _uiState

    private val _isLoading = MutableStateFlow(false) // Tracks if data is being loaded
    val isLoading: StateFlow<Boolean> = _isLoading

    private val loadedPodcasts = mutableListOf<Podcast>() // Holds all loaded podcasts
    private var currentPage = 1 // Tracks the current page
    private var hasNextPage = true // Indicates if more pages are available

    init {
        loadPodcasts()
    }

    fun loadPodcasts() {
        if (_isLoading.value || !hasNextPage) return // Avoid multiple or unnecessary requests

        _isLoading.value = true
        viewModelScope.launch {
            podcastUseCases.getPodcastsUseCase(currentPage).collect { result ->
                when (result) {
                    is ResultResponse.Success -> {
                        loadedPodcasts.addAll(result.data)
                        _uiState.value = ResultResponse.Success(loadedPodcasts)

                        // Update pagination flags
                        hasNextPage = result.data.isNotEmpty() // Check if there are items to load
                        if (hasNextPage) currentPage++
                    }
                    is ResultResponse.Error -> {
                        _uiState.value = result
                    }
                    else -> Unit
                }
                _isLoading.value = false
            }
        }
    }

    fun toggleFavourite(podcast: Podcast) {
        viewModelScope.launch {
            try {
                podcastUseCases.toggleFavouriteUseCase(podcast)

                // Update the favorite status locally
                val updatedPodcasts = loadedPodcasts.map {
                    if (it.id == podcast.id) it.copy(isFavourite = !podcast.isFavourite) else it
                }
                loadedPodcasts.clear()
                loadedPodcasts.addAll(updatedPodcasts)

                _uiState.value = ResultResponse.Success(loadedPodcasts)
            } catch (e: Exception) {
                _uiState.value = ResultResponse.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}
