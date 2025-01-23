package com.audiobooks.podcasts.data.local.database


import com.audiobooks.podcasts.data.local.dao.PodcastDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.audiobooks.podcasts.data.local.PodcastEntity

@Database(entities = [PodcastEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
}
