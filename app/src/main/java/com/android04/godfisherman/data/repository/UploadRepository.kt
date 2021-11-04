package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import javax.inject.Inject

class UploadRepository @Inject constructor(
    val localDataSource: UploadDataSource.LocalDataSource,
    val remoteDataSource: UploadDataSource.RemoteDataSource
) {

    suspend fun fetchFishTypeList(): List<String> = remoteDataSource.fetchFishTypeList()
}
