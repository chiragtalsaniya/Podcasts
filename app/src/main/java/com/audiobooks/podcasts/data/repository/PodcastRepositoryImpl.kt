package com.audiobooks.podcasts.data.repository

import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.data.mappers.toDomain
import com.audiobooks.podcasts.data.mappers.toEntity
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastRepositoryImpl @Inject constructor(
    private val apiService: PodcastApiService,
    private val podcastDao: PodcastDao
) : PodcastRepository {

    override suspend fun getPodcasts(page: Int): Flow<List<Podcast>> {
        // Fetch from API and cache in the database
        try {
            val apiResponse = apiService.getPodcasts(page)
            podcastDao.insertPodcasts(apiResponse.podcasts.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace() // Handle network errors gracefully
        }

        // Return podcasts from the database as a Flow
        return podcastDao.getAllPodcasts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavourite(podcast: Podcast) {
        // Update the favourite status in the database
        podcastDao.updateFavouriteStatus(podcast.id, !podcast.isFavourite)
    }
}
