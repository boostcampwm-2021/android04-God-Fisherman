package com.android04.godfisherman.data.datasource.uploadDataSource.local

import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.localdatabase.dao.TemporaryFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord
import javax.inject.Inject

class UploadLocalDataSourceImpl @Inject constructor(private val temporaryFishingRecordDao: TemporaryFishingRecordDao) :
    UploadDataSource.LocalDataSource {
    override suspend fun saveTmpTimeLineRecord(record: TemporaryFishingRecord) {
        temporaryFishingRecordDao.insert(record)
    }

}