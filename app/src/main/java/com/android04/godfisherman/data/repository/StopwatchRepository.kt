package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.SharedPreferenceManager
import com.google.firebase.Timestamp
import java.util.*
import javax.inject.Inject

class StopwatchRepository @Inject constructor(
    private val localDataSource: StopwatchDataSource.LocalDataSource,
    private val remoteDataSource: UploadDataSource.RemoteDataSource,
    private val sharedPreferenceManager: SharedPreferenceManager
) {
    suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord> =
        localDataSource.loadTmpTimeLineRecord()

    suspend fun saveTimeLineRecord(time: Double) {
        val list = loadTmpTimeLineRecord()
        val recordList = mutableListOf<FishingRecord>()

        list.forEachIndexed { index, record ->
            val imageUrl = remoteDataSource.getImageUrl(record.image)
            if (imageUrl != null) {
                recordList.add(
                    FishingRecord(
                        index,
                        imageUrl,
                        Date(),
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
                "user1"
            )

            remoteDataSource.saveTimeLineType(type, recordList)
            removeTmpTimeLineRecord()
        }
    }

    suspend fun removeTmpTimeLineRecord() = localDataSource.removeTmpTimeLineRecord()
}
