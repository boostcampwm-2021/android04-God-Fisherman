package com.android04.godfisherman.data.datasource.stopwatchdatasource.local

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.localdatabase.dao.TemporaryFishingRecordDao
import javax.inject.Inject

class StopwatchLocalDataSourceImpl @Inject constructor(
    private val temporaryFishingRecordDao: TemporaryFishingRecordDao
) : StopwatchDataSource.LocalDataSource {

}
