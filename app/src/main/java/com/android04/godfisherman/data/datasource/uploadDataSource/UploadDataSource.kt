package com.android04.godfisherman.data.datasource.uploadDataSource

import android.graphics.Bitmap
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord

interface UploadDataSource {

    interface LocalDataSource {
        suspend fun saveTmpTimeLineRecord(record: TmpFishingRecord)
    }

    interface RemoteDataSource {
        suspend fun fetchFishTypeList(): List<String>
        suspend fun getImageUrl(bitmap: Bitmap): String?
        suspend fun saveImageType(typeInfo: TypeInfo, fishingRecord: FishingRecord)
        suspend fun saveTimeLineType(typeInfo: TypeInfo, fishingRecordList: List<FishingRecord>)
    }
}