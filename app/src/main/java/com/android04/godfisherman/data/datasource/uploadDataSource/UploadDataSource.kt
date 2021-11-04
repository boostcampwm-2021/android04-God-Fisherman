package com.android04.godfisherman.data.datasource.uploadDataSource

interface UploadDataSource {

    interface LocalDataSource {
    }

    interface RemoteDataSource {
        suspend fun fetchFishTypeList(): List<String>
    }
}