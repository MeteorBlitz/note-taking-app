package com.example.note_taking_app.utils

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val exception: Throwable) : Resource<T>()
    class Loading<T> : Resource<T>()
}