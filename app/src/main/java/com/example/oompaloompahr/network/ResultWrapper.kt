package com.example.oompaloompahr.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class HttpError(val code: Int? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}