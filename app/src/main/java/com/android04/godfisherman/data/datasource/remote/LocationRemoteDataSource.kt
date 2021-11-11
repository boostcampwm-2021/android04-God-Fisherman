package com.android04.godfisherman.data.datasource.remote

interface LocationRemoteDataSource {
    suspend fun fetchAddress(latitude: Double, longitude: Double): String?
}
