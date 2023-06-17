package com.example.swipeassignment.model

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>(data = data)
    data class Error<T>(val message: String) : Resource<T>(message = message)
}