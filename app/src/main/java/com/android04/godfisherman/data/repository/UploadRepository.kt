package com.android04.godfisherman.data.repository

import android.graphics.Bitmap
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.RepoResponse
import com.android04.godfisherman.utils.SharedPreferenceManager
import com.google.firebase.Timestamp
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val localDataSource: UploadDataSource.LocalDataSource,
    private val remoteDataSource: UploadDataSource.RemoteDataSource,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    suspend fun fetchFishTypeList(callback: RepoResponse<Unit>): List<String> {
        var isSuccess = true
        var ret = listOf<String>()

        try {
            ret = remoteDataSource.fetchFishTypeList()
        } catch (e: Exception) {
            isSuccess = false
        } finally {
            callback.invoke(isSuccess, Unit)
        }

        return ret
    }

    suspend fun saveImageType(image: Bitmap, fishLength: Double, fishType: String, callback: RepoResponse<Unit>) {
        val imageUrl = remoteDataSource.getImageUrl(image)

        imageUrl?.let {
            val type = Type(
                Timestamp(Date()),
                false,
                sharedPreferenceManager.getString(SharedPreferenceManager.PREF_LOCATION) ?: "",
                0,
                "user1"
            )
            val fishingRecord = FishingRecord(0, imageUrl, Date(), fishLength, fishType)
            var isSuccess = true

            try {
                remoteDataSource.saveImageType(type, fishingRecord)
            } catch (e: Exception) {
                isSuccess = false
            } finally {
                callback.invoke(isSuccess, Unit)
            }
        }
    }

    suspend fun saveTmpTimeLineRecord(image: Bitmap, fishLength: Double, fishType: String, callback: RepoResponse<Unit>){
        val fishingRecord = TmpFishingRecord(image, Date(), fishLength, fishType)
        var isSuccess = true

        try {
            localDataSource.saveTmpTimeLineRecord(fishingRecord)
        } catch (e: Exception) {
            isSuccess = false
        } finally {
            callback.invoke(isSuccess, Unit)
        }
    }
}
