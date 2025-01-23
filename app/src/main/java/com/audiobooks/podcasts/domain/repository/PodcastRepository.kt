package com.audiobooks.podcasts.domain.repository

import com.audiobooks.podcasts.domain.model.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    suspend fun getPodcasts(page: Int): Flow<List<Podcast>>
    suspend fun toggleFavourite(podcast: Podcast)
}
