package com.audiobooks.podcasts.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.audiobooks.podcasts.data.local.PodcastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {

    @Query("SELECT * FROM podcasts")
    fun getAllPodcasts(): Flow<List<PodcastEntity>>

    @Query("SELECT * FROM podcasts WHERE isFavourite = 1")
    fun getFavouritePodcasts(): Flow<List<PodcastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)

    @Query("UPDATE podcasts SET isFavourite = :isFavourite WHERE id = :podcastId")
    suspend fun updateFavouriteStatus(podcastId: String, isFavourite: Boolean)

    @Query("SELECT * FROM podcasts WHERE id = :podcastId LIMIT 1")
    suspend fun getPodcastById(podcastId: String): PodcastEntity?
}
