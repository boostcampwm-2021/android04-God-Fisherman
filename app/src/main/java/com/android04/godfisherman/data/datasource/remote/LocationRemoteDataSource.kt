package com.android04.godfisherman.data.datasource.remote

import android.location.Address

interface LocationRemoteDataSource {
    suspend fun fetchAddress(latitude: Double, longitude: Double): Address
}
