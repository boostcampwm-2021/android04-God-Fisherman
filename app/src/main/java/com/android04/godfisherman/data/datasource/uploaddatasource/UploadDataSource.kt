package com.android04.godfisherman.data.datasource.uploaddatasource

import android.graphics.Bitmap
import com.android04.godfisherman.data.dto.FishingRecord
import com.android04.godfisherman.data.dto.TypeInfo
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord

interface UploadDataSource {

    interface LocalDataSource {
        suspend fun saveTmpTimeLineRecord(record: TmpFishingRecord)
        suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord>
        suspend fun removeTmpTimeLineRecord()
    }

    interface RemoteDataSource {
        suspend fun fetchFishTypeList(): List<String>
        suspend fun getImageUrl(bitmap: Bitmap): String?
        suspend fun saveImageType(typeInfo: TypeInfo, fishingRecord: FishingRecord)
        suspend fun saveTimeLineType(typeInfo: TypeInfo, fishingRecordList: List<FishingRecord>): Boolean
    }
}