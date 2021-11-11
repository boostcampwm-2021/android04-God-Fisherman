package com.android04.godfisherman.data.datasource.uploadDataSource.local

import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import javax.inject.Inject

class UploadLocalDataSourceImpl @Inject constructor(private val tmpFishingRecordDao: TmpFishingRecordDao) :
    UploadDataSource.LocalDataSource {
    override suspend fun saveTmpTimeLineRecord(record: TmpFishingRecord) {
        tmpFishingRecordDao.insert(record)
    }

}