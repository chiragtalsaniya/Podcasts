package com.audiobooks.podcasts.data.remote.api

import com.audiobooks.podcasts.data.remote.model.PodcastApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastApiService {

    @GET("best_podcasts")
    suspend fun getPodcasts(@Query("page") page: Int = 1): PodcastApiResponse

}