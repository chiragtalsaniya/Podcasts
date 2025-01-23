package com.audiobooks.podcasts.di

import com.audiobooks.podcasts.domain.repository.PodcastRepository
import com.audiobooks.podcasts.domain.usecase.GetPodcastsUseCase
import com.audiobooks.podcasts.domain.usecase.PodcastUseCases
import com.audiobooks.podcasts.domain.usecase.ToggleFavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providePodcastUseCases(repository: PodcastRepository): PodcastUseCases {
        return PodcastUseCases(
            getPodcastsUseCase = GetPodcastsUseCase(repository),
            toggleFavouriteUseCase = ToggleFavouriteUseCase(repository)
        )
    }
}
