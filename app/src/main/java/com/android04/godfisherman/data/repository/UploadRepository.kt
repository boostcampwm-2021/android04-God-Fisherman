package com.android04.godfisherman.data.repository

import android.graphics.Bitmap
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.SharedPreferenceManager
import com.google.firebase.Timestamp
import java.util.*
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val localDataSource: UploadDataSource.LocalDataSource,
    private val remoteDataSource: UploadDataSource.RemoteDataSource,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    suspend fun fetchFishTypeList(): List<String> = remoteDataSource.fetchFishTypeList()

    suspend fun saveImageType(image: Bitmap, fishLength: Double, fishType: String) {
        val imageUrl = remoteDataSource.getImageUrl(image)

        imageUrl?.let {
            val type = Type(Timestamp(Date()),false, "", 0, "user1")
            val fishingRecord = FishingRecord(0, imageUrl, Date(), fishLength, fishType)

            remoteDataSource.saveImageType(type, fishingRecord)
        }
    }

    suspend fun saveTmpTimeLineRecord(image: Bitmap, fishLength: Double, fishType: String){
        val fishingRecord = TmpFishingRecord(image, Date(), fishLength, fishType)
        localDataSource.saveTmpTimeLineRecord(fishingRecord)
    }

}
