package com.audiobooks.podcasts.di

import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.data.repository.PodcastRepositoryImpl
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePodcastRepository(
        apiService: PodcastApiService,
        podcastDao: PodcastDao
    ): PodcastRepository {
        return PodcastRepositoryImpl(apiService, podcastDao)
    }
}
