package com.audiobooks.podcasts.data.repository

import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.mappers.toDomain
import com.audiobooks.podcasts.data.mappers.toEntity
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import com.audiobooks.podcasts.utils.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastRepositoryImpl @Inject constructor(
    private val apiService: PodcastApiService,
    private val podcastDao: PodcastDao
) : PodcastRepository {

    override suspend fun getPodcasts(page: Int): Flow<ResultResponse<List<Podcast>>> {
        return flow {
            // Fetch podcasts from API
            try {
                val apiResponse = apiService.getPodcasts(page)
                podcastDao.insertPodcasts(apiResponse.podcasts.map { it.toEntity() })
            } catch (e: Exception) {
                emit(ResultResponse.Error(e)) // Emit error if network call fails
            }

            // Emit data from local database
            podcastDao.getAllPodcasts().map { entities ->
                entities.map { it.toDomain() }
            }.collect { podcasts ->
                emit(ResultResponse.Success(podcasts)) // Emit successful response
            }
        }.catch { e ->
            emit(ResultResponse.Error(Exception(e))) // Catch and emit errors
        }
    }

    override suspend fun toggleFavourite(podcast: Podcast) {
        try {
            // Update the favourite status in the database
            podcastDao.updateFavouriteStatus(podcast.id, !podcast.isFavourite)
        } catch (e: Exception) {
            e.printStackTrace() // Log error
        }
    }
}
