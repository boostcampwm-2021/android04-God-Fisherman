package com.android04.godfisherman.common.di

import com.android04.godfisherman.data.repository.HomeRepositoryImpl
import com.android04.godfisherman.data.repository.MainViewRepositoryImpl
import com.android04.godfisherman.data.repository.RecordRepositoryImpl
import com.android04.godfisherman.domain.HomeRepository
import com.android04.godfisherman.domain.MainViewRepository
import com.android04.godfisherman.domain.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryDiModule {

    @Binds
    abstract fun bindUploadRepository(
        recordRepositoryImpl: RecordRepositoryImpl
    ): RecordRepository.UploadRepository

    @Binds
    abstract fun bindStopwatchRepository(
        recordRepositoryImpl: RecordRepositoryImpl
    ): RecordRepository.StopwatchRepository

    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    abstract fun bindMainViewRepository(
        mainViewRepositoryImpl: MainViewRepositoryImpl
    ): MainViewRepository
}
