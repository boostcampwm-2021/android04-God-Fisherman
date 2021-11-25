package com.android04.godfisherman.data.datasource.uploaddatasource.local

import com.android04.godfisherman.data.datasource.uploaddatasource.UploadDataSource
import com.android04.godfisherman.data.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord
import javax.inject.Inject

class UploadLocalDataSourceImpl @Inject constructor(private val tmpFishingRecordDao: TmpFishingRecordDao) :
    UploadDataSource.LocalDataSource {
    override suspend fun saveTmpTimeLineRecord(record: TmpFishingRecord) {
        tmpFishingRecordDao.insert(record)
    }

}