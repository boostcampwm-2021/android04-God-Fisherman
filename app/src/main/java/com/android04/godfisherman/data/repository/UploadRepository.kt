package com.android04.godfisherman.data.repository

import android.graphics.Bitmap
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
import java.util.*
import javax.inject.Inject

class UploadRepository @Inject constructor(
    val localDataSource: UploadDataSource.LocalDataSource,
    val remoteDataSource: UploadDataSource.RemoteDataSource
) {

    suspend fun fetchFishTypeList(): List<String> = remoteDataSource.fetchFishTypeList()

    suspend fun saveImageType(image: Bitmap, fishLength: Double, fishType: String) {
        val imageUrl = remoteDataSource.getImageUrl(image)

        imageUrl?.let {
            val type = Type(false, "", 0, "user1")
            val fishingRecord = FishingRecord(imageUrl, Date(), fishLength, fishType)

            remoteDataSource.saveImageType(type, fishingRecord)
        }
    }

}
