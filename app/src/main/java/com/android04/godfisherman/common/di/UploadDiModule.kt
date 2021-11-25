package com.android04.godfisherman.common.di

import com.android04.godfisherman.data.datasource.uploaddatasource.UploadDataSource
import com.android04.godfisherman.data.datasource.uploaddatasource.local.UploadLocalDataSourceImpl
import com.android04.godfisherman.data.datasource.uploaddatasource.remote.UploadRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UploadDiModule {

    @Binds
    abstract fun bindUploadLocalDataSource(
        uploadLocalDataSourceImpl: UploadLocalDataSourceImpl
    ): UploadDataSource.LocalDataSource

    @Binds
    abstract fun bindUploadRemoteDataSource(
        uploadRemoteDataSourceImpl: UploadRemoteDataSourceImpl
    ): UploadDataSource.RemoteDataSource
}