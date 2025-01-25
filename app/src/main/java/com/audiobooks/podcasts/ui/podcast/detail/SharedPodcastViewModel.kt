package com.audiobooks.podcasts.ui.podcast.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.usecase.PodcastUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedPodcastViewModel @Inject constructor(
    private val podcastUseCases: PodcastUseCases
) : ViewModel() {


    // State to hold the currently selected podcast
    private val _selectedPodcast = MutableStateFlow<Podcast?>(null)
    val selectedPodcast: StateFlow<Podcast?> = _selectedPodcast

    // Function to update the selected podcast
    fun selectPodcast(podcast: Podcast) {
        _selectedPodcast.value = podcast
    }

    // Function to toggle the favourite status of the podcast
    fun toggleFavourite() {
        val currentPodcast = _selectedPodcast.value
        if (currentPodcast != null) {
            // Update the local state
            val updatedPodcast = currentPodcast.copy(isFavourite = !currentPodcast.isFavourite)
            _selectedPodcast.value = updatedPodcast

            // Persist the change using the use case
            viewModelScope.launch {
                try {
                    podcastUseCases.toggleFavouriteUseCase(updatedPodcast)
                } catch (e: Exception) {
                    // Handle any errors (e.g., rollback the local change if necessary)
                    _selectedPodcast.value = currentPodcast
                }
            }
        }
    }
}
