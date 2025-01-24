package com.audiobooks.podcasts.domain.usecase

import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import com.audiobooks.podcasts.utils.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetPodcastsUseCase @Inject constructor(
    private val repository: PodcastRepository
) {
    suspend operator fun invoke(page: Int): Flow<ResultResponse<List<Podcast>>> {
        return repository.getPodcasts(page)
            .catch { e ->
                emit(ResultResponse.Error(e.message ?: "Failed to load podcasts"))
            }
    }
}

