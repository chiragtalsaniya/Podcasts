package com.audiobooks.podcasts.domain.repository

import com.audiobooks.podcasts.domain.model.Podcast

interface PodcastRepository {
    suspend fun getPodcasts(page: Int): List<Podcast>
    suspend fun toggleFavourite(podcast: Podcast)
}
