package com.audiobooks.podcasts.domain.usecase

import javax.inject.Inject

data class PodcastUseCases @Inject constructor(
    val getPodcastsUseCase: GetPodcastsUseCase,
    val toggleFavouriteUseCase: ToggleFavouriteUseCase
)