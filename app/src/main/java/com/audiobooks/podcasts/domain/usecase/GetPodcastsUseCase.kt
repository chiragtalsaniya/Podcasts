package com.audiobooks.podcasts.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.audiobooks.podcasts.domain.model.Podcast
import com.audiobooks.podcasts.domain.repository.PodcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPodcastsUseCase @Inject constructor(
    private val podcastRepository: PodcastRepository
) {
    operator fun invoke(): Flow<PagingData<Podcast>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,  // Loads 20 items per page
                prefetchDistance = 5, // Prefetch when 5 items before the end
                enablePlaceholders = false
            ),
            pagingSourceFactory = { podcastRepository.getPodcastPagingSource() }
        ).flow
    }
}
