package com.audiobooks.podcasts.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.audiobooks.podcasts.data.local.dao.PodcastDao
import com.audiobooks.podcasts.data.mappers.toDomain
import com.audiobooks.podcasts.data.mappers.toEntity
import com.audiobooks.podcasts.data.remote.api.PodcastApiService
import com.audiobooks.podcasts.domain.model.Podcast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class PodcastPagingSource @Inject constructor(
    private val apiService: PodcastApiService, private val podcastDao: PodcastDao
) : PagingSource<Int, Podcast>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Podcast> {
        val page = params.key ?: 1 // Start from page 1
        return try {
            delay(2000) // Simulate network delay for API response

            // Fetch remote data
            val response = apiService.getPodcasts(page)
            val remotePodcasts = response.podcasts.map { it.toEntity() }

            // Fetch local database to merge `isFavourite` status
            val localData = podcastDao.getAllPodcasts().firstOrNull() ?: emptyList()

            // Merge remote data with local `isFavourite` state
            val mergedPodcasts = remotePodcasts.map { remoteEntity ->
                val localEntity = localData.find { it.id == remoteEntity.id }
                remoteEntity.copy(isFavourite = localEntity?.isFavourite ?: false)
            }

            // Save merged data into Room Database
            podcastDao.insertPodcasts(mergedPodcasts)

            LoadResult.Page(
                data = mergedPodcasts.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1 // manual increment
            )
        } catch (e: IOException) {
            LoadResult.Error(e) // Handle network errors
        } catch (e: HttpException) {
            LoadResult.Error(e) // Handle HTTP errors
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Podcast>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
