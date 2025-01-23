package com.audiobooks.podcasts.domain.repository

import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.utils.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    suspend fun getPodcasts(page: Int): Flow<ResultResponse<List<Podcast>>>
    suspend fun toggleFavourite(podcast: Podcast)
}
