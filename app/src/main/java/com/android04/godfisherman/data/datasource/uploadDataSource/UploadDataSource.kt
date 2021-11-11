package com.android04.godfisherman.data.datasource.uploadDataSource

import android.graphics.Bitmap
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord

interface UploadDataSource {

    interface LocalDataSource {
        suspend fun saveTmpTimeLineRecord(record: TemporaryFishingRecord)
    }

    interface RemoteDataSource {
        suspend fun fetchFishTypeList(): List<String>
        suspend fun getImageUrl(bitmap: Bitmap): String?
        suspend fun saveImageType(type: Type, fishingRecord: FishingRecord)
    }
}