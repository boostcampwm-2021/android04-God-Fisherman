package com.android04.godfisherman.data.datasource.stopwatchdatasource

import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord

interface StopwatchDataSource {

    interface LocalDataSource{
        suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord>
        suspend fun removeTmpTimeLineRecord()
    }

    interface RemoteDataSource{}
}
