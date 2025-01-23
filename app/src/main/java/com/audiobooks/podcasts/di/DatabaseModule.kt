package com.audiobooks.podcasts.di

import android.content.Context
import androidx.room.Room
import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "podcasts_database"
        ).build()
    }

    @Provides
    fun providePodcastDao(appDatabase: AppDatabase): PodcastDao {
        return appDatabase.podcastDao()
    }
}
