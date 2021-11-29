package com.android04.godfisherman.common.di

import com.android04.godfisherman.data.datasource.stopwatchdatasource.StopwatchDataSource
import com.android04.godfisherman.data.datasource.stopwatchdatasource.local.StopwatchLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class StopwatchDiModule {

    @Binds
    abstract fun bindStopwatchLocalDataSource(
        localDataSourceImpl: StopwatchLocalDataSourceImpl
    ) : StopwatchDataSource.LocalDataSource
}
