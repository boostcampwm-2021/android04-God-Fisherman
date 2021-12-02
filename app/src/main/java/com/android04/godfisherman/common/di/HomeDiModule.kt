package com.android04.godfisherman.common.di

import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.data.datasource.homedatasource.remote.HomeRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeDiModule {

    @Binds
    abstract fun bindHomeRemoteDataSource(
        homeRemoteDataSourceImpl: HomeRemoteDataSourceImpl
    ): HomeDataSource.RemoteDataSource
}