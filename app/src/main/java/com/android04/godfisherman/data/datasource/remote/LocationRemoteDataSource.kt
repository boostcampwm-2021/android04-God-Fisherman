package com.android04.godfisherman.data.datasource.remote

import android.location.Address

interface LocationRemoteDataSource {
    fun fetchAddress(latitude: Double, longitude: Double): String
}
