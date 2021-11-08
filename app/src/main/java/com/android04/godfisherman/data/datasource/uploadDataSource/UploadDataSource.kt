package com.android04.godfisherman.data.datasource.uploadDataSource

import android.graphics.Bitmap

interface UploadDataSource {

    interface LocalDataSource {
    }

    interface RemoteDataSource {
        suspend fun fetchFishTypeList(): List<String>
        suspend fun getImageUrl(bitmap: Bitmap): String?
    }
}