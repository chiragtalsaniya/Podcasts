package com.audiobooks.podcasts.data.mappers

import com.audiobooks.podcasts.data.local.PodcastEntity
import com.audiobooks.podcasts.domain.model.Podcast

fun PodcastEntity.toDomain(): Podcast {
    return Podcast(
        id = id,
        title = title,
        description = description,
        publisher = publisher,
        totalEpisodes = 0, // Default value
        thumbnail = thumbnail,
        image = "", // Default value
        rss = null, // Default value
        website = null, // Default value
        genreIds = emptyList(), // Default value
        language = "", // Default value
        country = "", // Default value
        explicitContent = false, // Default value
        listennotesUrl = "", // Default value
        audioLengthSec = 0, // Default value
        latestPubDateMs = 0L, // Default value
        earliestPubDateMs = 0L, // Default value
        hasSponsors = false, // Default value
        email = null, // Default value
        latestEpisodeId = "", // Default value
        isClaimed = false, // Default value
        listenScore = 0, // Default value
        listenScoreGlobalRank = "", // Default value
        hasGuestInterviews = false, // Default value
        lookingFor = null, // Default value
        socialLinks = null, // Default value
        isFavourite = isFavourite
    )
}

fun Podcast.toEntity(): PodcastEntity {
    return PodcastEntity(
        id = id,
        title = title,
        description = description,
        publisher = publisher,
        thumbnail = thumbnail,
        isFavourite = isFavourite,
    )
}
