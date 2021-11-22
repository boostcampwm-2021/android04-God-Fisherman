package com.android04.godfisherman.common

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Fail<T>(val description: String) : Result<T>()
}
