package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import javax.inject.Inject

class StopwatchRepository @Inject constructor(
    private val localDataSource: StopwatchDataSource.LocalDataSource
) {

}
