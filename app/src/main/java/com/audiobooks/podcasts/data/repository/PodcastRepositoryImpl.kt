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
            delay(3000) // Simulate 3-second delay

            try {
                // Fetch remote data
                val apiResponse = apiService.getPodcasts(page)

                // Update the local database
                podcastDao.insertPodcasts(apiResponse.podcasts.map { it.toEntity() })

                // Emit updated local data
                podcastDao.getAllPodcasts().map { entities ->
                    entities.map { it.toDomain() }
                }.collect { updatedData ->
                    emit(ResultResponse.Success(updatedData))
                }
            } catch (e: Exception) {
                emit(ResultResponse.Error(e.localizedMessage?:"An unknown error occurred")) // Emit error if API call fails
            }
        }.catch { e ->
            emit(ResultResponse.Error(e.localizedMessage?:"An unknown error occurred"))
        }
    }

    override suspend fun toggleFavourite(podcast: Podcast) {
        podcastDao.updateFavouriteStatus(podcast.id, !podcast.isFavourite)
    }
}
