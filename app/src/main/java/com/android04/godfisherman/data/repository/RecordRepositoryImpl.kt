package com.android04.godfisherman.data.repository

import android.graphics.Bitmap
import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.SharedPreferenceManager
import com.android04.godfisherman.data.datasource.uploaddatasource.UploadDataSource
import com.android04.godfisherman.data.dto.FishingRecord
import com.android04.godfisherman.data.dto.TypeInfo
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.domain.RecordRepository
import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_NAME
import com.android04.godfisherman.utils.StorageManager
import com.android04.godfisherman.utils.calculateRecordSize
import com.google.firebase.Timestamp
import java.util.*
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val localDataSource: UploadDataSource.LocalDataSource,
    private val remoteDataSource: UploadDataSource.RemoteDataSource,
    private val sharedPreferenceManager: SharedPreferenceManager
) : RecordRepository.UploadRepository, RecordRepository.StopwatchRepository {

    override suspend fun fetchFishTypeList(callback: RepoResponse<Unit>): List<String> {
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

    override suspend fun saveImageType(
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

    override suspend fun saveTmpTimeLineRecord(
        image: Bitmap,
        fishLength: Double,
        fishType: String,
        callback: RepoResponse<Unit>
    ) {
        val fishingRecord = TmpFishingRecord(image, Date(), fishLength, fishType)
        val size = calculateRecordSize(fishingRecord)

        var isSuccess = StorageManager.getInternalRemainMemory() > size

        try {
            if (isSuccess) {
                localDataSource.saveTmpTimeLineRecord(fishingRecord)
            }
        } catch (e: Exception) {
            isSuccess = false
        } finally {
            callback.invoke(isSuccess, Unit)
        }
    }

    override fun getAddress(): String =
        sharedPreferenceManager.getString(SharedPreferenceManager.PREF_LOCATION) ?: ""

    override suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord> =
        localDataSource.loadTmpTimeLineRecord()

    override suspend fun saveTimeLineRecord(time: Double): Result<Boolean> {
        val list = loadTmpTimeLineRecord()
        val recordList = mutableListOf<FishingRecord>()

        list.forEachIndexed { index, record ->
            val imageUrl = remoteDataSource.getImageUrl(record.image)
            if (imageUrl != null) {
                recordList.add(
                    FishingRecord(
                        index,
                        imageUrl,
                        record.date,
                        record.fishLength,
                        record.fishType
                    )
                )
            }
        }
        if (list.size == recordList.size) {
            val type = TypeInfo(
                Timestamp(Date()),
                true,
                sharedPreferenceManager.getString(SharedPreferenceManager.PREF_LOCATION)
                    ?: "위치 정보 없음",
                time.toInt(),
                sharedPreferenceManager.getString(LOGIN_NAME) ?: "유저 이름 없음"
            )

            if (remoteDataSource.saveTimeLineType(type, recordList)) {
                removeTmpTimeLineRecord()
                return Result.Success(true)
            }
        }

        return Result.Fail("업로드에 실패했습니다 다시 시도해주세요")
    }

    private suspend fun removeTmpTimeLineRecord() = localDataSource.removeTmpTimeLineRecord()
}
