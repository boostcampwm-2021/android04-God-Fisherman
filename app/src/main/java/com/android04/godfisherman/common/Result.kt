package com.android04.godfisherman.common

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Fail(val description: String) : Result<Nothing>()
}
