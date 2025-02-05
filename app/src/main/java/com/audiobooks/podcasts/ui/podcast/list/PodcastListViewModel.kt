package com.audiobooks.podcasts.ui.podcast.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.mappers.toDomain
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.usecase.PodcastUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastListViewModel @Inject constructor(
    private val podcastUseCases: PodcastUseCases,
    private val podcastDao: PodcastDao
) : ViewModel() {

    // Trigger paging refresh when database changes
    private val _databaseUpdateTrigger = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val podcasts: Flow<PagingData<Podcast>> = _databaseUpdateTrigger.flatMapLatest {
        podcastUseCases.getPodcastsUseCase()
            .map { pagingData -> pagingData.map { updatePodcastFavouriteStatus(it) } }
    }.cachedIn(viewModelScope)

    init {
        observeDatabaseChanges()
    }

    /**
     * Observe database changes and trigger paging refresh
     */
    private fun observeDatabaseChanges() {
        viewModelScope.launch {
            podcastDao.getAllPodcasts()
                .map { entities -> entities.map { it.toDomain() } }
                .collect {
                    _databaseUpdateTrigger.value = Unit
                }
        }
    }

    /**
     * Updates the favourite status of a podcast by checking Room database
     */
    private suspend fun updatePodcastFavouriteStatus(podcast: Podcast): Podcast {
        val dbPodcast = podcastDao.getPodcastById(podcast.id)
        return if (dbPodcast != null) {
            podcast.copy(isFavourite = dbPodcast.isFavourite)
        } else {
            podcast
        }
    }

}


