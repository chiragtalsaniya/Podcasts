package com.audiobooks.podcasts.domain.model

data class LookingFor(
    val crossPromotion: Boolean,
    val sponsors: Boolean,
    val guests: Boolean,
    val cohosts: Boolean
)