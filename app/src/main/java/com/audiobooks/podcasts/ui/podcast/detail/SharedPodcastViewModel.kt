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

    private val _selectedPodcast = MutableStateFlow<Podcast?>(null)
    val selectedPodcast: StateFlow<Podcast?> = _selectedPodcast

    fun selectPodcast(podcast: Podcast) {
        _selectedPodcast.value = podcast
    }

    /**
     * Toggles the favourite status and persists it to Room Database
     */
    fun toggleFavourite() {
        val currentPodcast = _selectedPodcast.value
        if (currentPodcast != null) {
            val updatedPodcast = currentPodcast.copy(isFavourite = !currentPodcast.isFavourite)
            _selectedPodcast.value = updatedPodcast

            viewModelScope.launch {
                try {
                    podcastUseCases.toggleFavouriteUseCase(updatedPodcast)
                } catch (e: Exception) {
                    _selectedPodcast.value = currentPodcast  // Rollback in case of error
                }
            }
        }
    }
}
