package com.android04.godfisherman.data.datasource.locationdatasource

interface LocationRemoteDataSource {
    suspend fun fetchAddress(latitude: Double, longitude: Double): String?
}
