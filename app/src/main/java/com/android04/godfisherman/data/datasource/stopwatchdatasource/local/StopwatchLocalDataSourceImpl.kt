package com.android04.godfisherman.data.datasource.stopwatchdatasource.local

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import javax.inject.Inject

class StopwatchLocalDataSourceImpl @Inject constructor(
    private val tmpFishingRecordDao: TmpFishingRecordDao
) : StopwatchDataSource.LocalDataSource {
    override suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord> = tmpFishingRecordDao.getTmpRecords()

}
