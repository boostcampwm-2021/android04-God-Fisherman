package com.android04.godfisherman.di

import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.data.datasource.feedDatasource.local.FeedLocalDataSourceImpl
import com.android04.godfisherman.data.datasource.feedDatasource.remote.FeedRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FeedDiModule {

    @Binds
    abstract fun bindFeedLocalDataSource(
        feedLocalDataSourceImpl: FeedLocalDataSourceImpl
    ): FeedDataSource.LocalDataSource

    @Binds
    abstract fun bindFeedRemoteDataSource(
        feedRemoteDataSourceImpl: FeedRemoteDataSourceImpl
    ): FeedDataSource.RemoteDataSource
}