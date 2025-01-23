package com.audiobooks.podcasts.domain.usecase

data class PodcastUseCases(
    val getPodcastsUseCase: GetPodcastsUseCase,
    val toggleFavouriteUseCase: ToggleFavouriteUseCase
)
