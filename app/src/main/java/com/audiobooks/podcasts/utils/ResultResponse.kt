package com.audiobooks.podcasts.utils

sealed class ResultResponse<out T> {
    data class Success<T>(val data: T) : ResultResponse<T>()
    data class Error(val exception: String) : ResultResponse<Nothing>()
    object Loading : ResultResponse<Nothing>()
}
