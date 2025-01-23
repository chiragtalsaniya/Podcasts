package com.audiobooks.podcasts.data.remote.model

import com.audiobooks.podcasts.domain.model.Podcast

data class PodcastApiResponse(
    val id: Int,
    val name: String,
    val total: Int,
    val hasNext: Boolean,
    val podcasts: List<Podcast>,
    val parentId: Int?,
    val pageNumber: Int,
    val hasPrevious: Boolean,
    val listennotesUrl: String,
    val nextPageNumber: Int?,
    val previousPageNumber: Int?
)