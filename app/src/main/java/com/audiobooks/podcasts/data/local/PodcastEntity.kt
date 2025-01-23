package com.audiobooks.podcasts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcasts")
data class PodcastEntity(
    @PrimaryKey val id: String,
    val title: String,
    val publisher: String,
    val thumbnail: String,
    val description: String,
    val isFavourite: Boolean = false // Indicates if the podcast is a favourite
)
