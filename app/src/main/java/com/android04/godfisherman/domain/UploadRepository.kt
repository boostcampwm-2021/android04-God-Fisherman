package com.android04.godfisherman.domain

import android.graphics.Bitmap
import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord

interface RecordRepository {
    interface UploadRepository {
        suspend fun fetchFishTypeList(callback: RepoResponse<Unit>): List<String>
        fun getAddress(): String
        suspend fun saveImageType(
            image: Bitmap,
            fishLength: Double,
            fishType: String,
            callback: RepoResponse<Unit>
        )

        suspend fun saveTmpTimeLineRecord(
            image: Bitmap,
            fishLength: Double,
            fishType: String,
            callback: RepoResponse<Unit>
        )
    }

    interface StopwatchRepository {
        suspend fun saveTimeLineRecord(time: Double): Result<Boolean>
        suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord>
    }
}