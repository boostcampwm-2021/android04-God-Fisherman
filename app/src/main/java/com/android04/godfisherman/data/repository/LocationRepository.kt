package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.remote.RemoteLocationDataSource
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: RemoteLocationDataSource
) {

    fun loadData() {
        remoteDataSource.loadData()
    }
}
