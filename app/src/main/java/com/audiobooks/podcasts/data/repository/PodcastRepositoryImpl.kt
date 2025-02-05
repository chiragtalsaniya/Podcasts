package com.audiobooks.podcasts.data.repository

import androidx.paging.PagingSource
import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import javax.inject.Inject

class PodcastRepositoryImpl @Inject constructor(
    private val apiService: PodcastApiService,
    private val podcastDao: PodcastDao
) : PodcastRepository {

    override fun getPodcastPagingSource(): PagingSource<Int, Podcast> {
        return PodcastPagingSource(apiService, podcastDao)
    }

    override suspend fun toggleFavourite(podcast: Podcast) {
        // Fetch the current favourite status from the database
        val currentPodcastEntity = podcastDao.getPodcastById(podcast.id)
        if (currentPodcastEntity != null) {
            // Toggle the isFavourite status
            val newFavouriteStatus = !currentPodcastEntity.isFavourite
            podcastDao.updateFavouriteStatus(podcast.id, newFavouriteStatus)
        }
    }
}

