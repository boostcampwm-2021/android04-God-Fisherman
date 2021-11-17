package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.ui.feed.FeedData
import com.android04.godfisherman.utils.*
import kotlinx.coroutines.*
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource,
    private val networkChecker: NetworkChecker
) {

    suspend fun getFeedDataList(type: Type): List<FeedData> {
        return when (networkChecker.isConnected()) {
            true -> fetch(type)
            false -> loadFeedDataList(type)
        }
    }

    private suspend fun fetch(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()
        val feedList = remoteDataSource.fetchFeedDataList(type)

        if (feedList != null) {
            feedList.forEach { feed ->
                when (feed.typeInfo.isTimeline) {
                    true -> list.add(feed.toFeedTimeLineData())
                    false -> list.add(feed.toFeedPhotoData())
                }
            }

            CoroutineScope(Dispatchers.IO + NonCancellable).launch {
                saveFeedListInCache(feedList)
            }
        } else {
            //todo
        }

        return list
    }


    private suspend fun loadFeedDataList(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()

        localDataSource.loadFeedDataList().forEach { feed ->
            when (feed.typeInfo.isTimeline) {
                true -> list.add(feed.toFeedTimeLineData())
                false -> list.add(feed.toFeedPhotoData())
            }
        }

        return list
    }

    private suspend fun saveFeedListInCache(feedList: List<FeedDTO>) {
        localDataSource.deleteAll()

        feedList.forEach { feed ->
            localDataSource.saveFeed(
                feed.typeInfo.toTypeInfoCached(),
                feed.fishingRecordList.map {
                    it.toFishingRecordCached(feed.typeInfo)
                }
            )
        }
    }
}
