package com.audiobooks.podcasts.domain.model

data class Podcast(
    val id: String,
    val title: String,
    val description: String,
    val publisher: String,
    val totalEpisodes: Int,
    val thumbnail: String,
    val image: String,
    val rss: String?,
    val website: String?,
    val genreIds: List<Int>,
    val language: String,
    val country: String,
    val explicitContent: Boolean,
    val listennotesUrl: String,
    val audioLengthSec: Int,
    val latestPubDateMs: Long,
    val earliestPubDateMs: Long,
    val hasSponsors: Boolean,
    val email: String?,
    val latestEpisodeId: String,
    val isClaimed: Boolean,
    val listenScore: Int,
    val listenScoreGlobalRank: String,
    val hasGuestInterviews: Boolean,
    val lookingFor: LookingFor?,
    val socialLinks: SocialLinks?,
    var isFavourite: Boolean = false // Default to false
)

