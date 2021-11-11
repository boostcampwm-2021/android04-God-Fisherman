package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import javax.inject.Inject

class StopwatchRepository @Inject constructor(
    private val localDataSource: StopwatchDataSource.LocalDataSource
) {
    suspend fun loadTmpTimeLineRecord():List<TmpFishingRecord> = localDataSource.loadTmpTimeLineRecord()
}
