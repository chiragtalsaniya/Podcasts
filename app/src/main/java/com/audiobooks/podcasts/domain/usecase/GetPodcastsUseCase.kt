package com.audiobooks.podcasts.domain.usecase

import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import javax.inject.Inject

class GetPodcastsUseCase @Inject constructor(
    private val repository: PodcastRepository
) {
    suspend operator fun invoke(page: Int): List<Podcast> = repository.getPodcasts(page)
}
