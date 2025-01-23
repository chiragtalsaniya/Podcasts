package com.audiobooks.podcasts.domain.usecase

import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPodcastsUseCase @Inject constructor(
    private val repository: PodcastRepository
) {
    suspend operator fun invoke(page: Int): Flow<List<Podcast>> = repository.getPodcasts(page)
}
