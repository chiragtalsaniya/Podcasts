package com.audiobooks.podcasts.domain.usecase

import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val repository: PodcastRepository
) {
    suspend operator fun invoke(podcast: Podcast) {
        repository.toggleFavourite(podcast)
    }
}
