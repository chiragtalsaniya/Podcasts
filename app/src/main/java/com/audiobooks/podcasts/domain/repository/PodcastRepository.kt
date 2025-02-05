package com.audiobooks.podcasts.domain.repository

import androidx.paging.PagingSource
import com.audiobooks.podcasts.domain.model.Podcast

interface PodcastRepository {
    fun getPodcastPagingSource(): PagingSource<Int, Podcast>
    suspend fun toggleFavourite(podcast: Podcast)
}
