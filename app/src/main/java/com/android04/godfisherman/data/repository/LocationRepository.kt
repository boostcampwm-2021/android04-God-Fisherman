package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource
) {
}
