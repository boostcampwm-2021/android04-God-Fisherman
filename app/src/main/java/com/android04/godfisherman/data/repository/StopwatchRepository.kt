package com.android04.godfisherman.data.repository

import android.util.Log
import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
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
    suspend fun loadTmpTimeLineRecord():List<TmpFishingRecord> = localDataSource.loadTmpTimeLineRecord()

    suspend fun saveTimeLineRecord(time:Double) {
        val recordList = mutableListOf<FishingRecord>()
        val list = loadTmpTimeLineRecord()
        list.forEach {
            val imageUrl = remoteDataSource.getImageUrl(it.image)
            if (imageUrl != null){
                recordList.add(FishingRecord(0, imageUrl, Date(), it.fishLength, it.fishType))
            }
        }
        if (list.size == recordList.size){
            val type = Type(
                Timestamp(Date()),
                true,
                sharedPreferenceManager.getString(SharedPreferenceManager.PREF_LOCATION) ?: "",
                time.toInt(),
                "user1"
            )
            Log.d("TAG", "saveTimeLineRecord: $recordList")
            remoteDataSource.saveTimeLineType(type, recordList)
        }
    }
}
