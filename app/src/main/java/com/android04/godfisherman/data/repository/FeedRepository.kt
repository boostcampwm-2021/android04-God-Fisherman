package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource
) {

}