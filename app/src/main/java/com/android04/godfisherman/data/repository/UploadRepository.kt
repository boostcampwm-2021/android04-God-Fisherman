package com.android04.godfisherman.data.repository

import android.graphics.Bitmap
import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.data.datasource.uploaddatasource.UploadDataSource
import com.android04.godfisherman.data.dto.FishingRecord
import com.android04.godfisherman.data.dto.TypeInfo
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_NAME
import com.android04.godfisherman.common.SharedPreferenceManager
import com.android04.godfisherman.utils.StorageManager
import com.android04.godfisherman.utils.calculateRecordSize
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

    suspend fun saveImageType(
        image: Bitmap,
        fishLength: Double,
        fishType: String,
        callback: RepoResponse<Unit>
    ) {
        val imageUrl = remoteDataSource.getImageUrl(image)

        imageUrl?.let {
            val type = TypeInfo(
                Timestamp(Date()),
                false,
                getAddress(),
                0,
                sharedPreferenceManager.getString(LOGIN_NAME) ?: "USER1"
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

    suspend fun saveTmpTimeLineRecord(
        image: Bitmap,
        fishLength: Double,
        fishType: String,
        callback: RepoResponse<Unit>
    ) {
        val fishingRecord = TmpFishingRecord(image, Date(), fishLength, fishType)
        val size = calculateRecordSize(fishingRecord)

        var isSuccess = StorageManager.getInternalRemainMemory() > size

        try {
            if(isSuccess) {
                localDataSource.saveTmpTimeLineRecord(fishingRecord)
            }
        } catch (e: Exception) {
            isSuccess = false
        } finally {
            callback.invoke(isSuccess, Unit)
        }
    }

    fun getAddress(): String =
        sharedPreferenceManager.getString(SharedPreferenceManager.PREF_LOCATION) ?: ""
}
