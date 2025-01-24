package com.audiobooks.podcasts.data.repository

import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.mappers.toDomain
import com.audiobooks.podcasts.data.mappers.toEntity
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import com.audiobooks.podcasts.utils.ResultResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
class PodcastRepositoryImpl @Inject constructor(
    private val apiService: PodcastApiService,
    private val podcastDao: PodcastDao
) : PodcastRepository {

    override suspend fun getPodcasts(page: Int): Flow<ResultResponse<List<Podcast>>> {
        return flow {
            delay(3000) // Simulate 3-second network delay

            try {
                // Fetch remote data
                val apiResponse = apiService.getPodcasts(page)
                val remotePodcasts = apiResponse.podcasts.map { it.toEntity() }

                // Fetch local data for favourite state
                val localData = podcastDao.getAllPodcasts().firstOrNull() ?: emptyList()

                // Merge favourite state from local data into remote data
                val mergedPodcasts = remotePodcasts.map { remoteEntity ->
                    val localEntity = localData.find { it.id == remoteEntity.id }
                    remoteEntity.copy(isFavourite = localEntity?.isFavourite ?: false)
                }

                // Update the local database with merged data
                podcastDao.insertPodcasts(mergedPodcasts)

                // Emit updated data from the local database
                podcastDao.getAllPodcasts().map { entities ->
                    entities.map { it.toDomain() }
                }.collect { updatedData ->
                    emit(ResultResponse.Success(updatedData))
                }
            } catch (e: Exception) {
                // On network error, fallback to local data
                podcastDao.getAllPodcasts().map { entities ->
                    entities.map { it.toDomain() }
                }.collect { localData ->
                    emit(ResultResponse.Success(localData))
                }
                // Emit error message for logging
                emit(ResultResponse.Error(e.localizedMessage ?: "An unknown error occurred"))
            }
        }.catch { e ->
            emit(ResultResponse.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
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

