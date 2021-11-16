package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.ui.feed.FeedData
import com.android04.godfisherman.ui.feed.FeedPhotoData
import com.android04.godfisherman.ui.feed.FeedTimelineData
import com.android04.godfisherman.ui.feed.TimeLineData
import com.android04.godfisherman.utils.getFeedDateFormat
import com.android04.godfisherman.utils.getFeedTimeFormat
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
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

    private suspend fun loadFeedDataList(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()

        localDataSource.loadFeedDataList()?.forEach { feed ->
            when (feed.typeInfo.isTimeline) {
                true -> {
                    val typeInfo = feed.typeInfo

                    list.add(
                        FeedTimelineData(
                            typeInfo.userName,
                            typeInfo.location,
                            getFeedDateFormat(typeInfo.id),
                            feed.fishingRecords.map { it.imageUrl },
                            feed.fishingRecords.map {
                                TimeLineData(
                                    it.fishType,
                                    it.fishLength,
                                    getFeedTimeFormat(it.date)
                                )
                            })
                    )
                }
                false -> {
                    val typeInfo = feed.typeInfo
                    val photo = feed.fishingRecords[0]

                    list.add(
                        FeedPhotoData(
                            typeInfo.userName,
                            typeInfo.location,
                            getFeedDateFormat(typeInfo.id),
                            photo.imageUrl,
                            photo.fishType,
                            photo.fishLength
                        )
                    )
                }
            }
        }

        return list
    }

    private suspend fun fetch(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()

        val feedList = remoteDataSource.fetchFeedDataList(type)

        if (feedList != null) {
            feedList.forEach { feed ->
                when (feed.typeInfo.isTimeline) {
                    true -> {
                        val typeInfo = feed.typeInfo

                        list.add(
                            FeedTimelineData(
                                typeInfo.userName,
                                typeInfo.location,
                                getFeedDateFormat(typeInfo.id.toDate()),
                                feed.fishingRecordList.map { it.imageUrl },
                                feed.fishingRecordList.map {
                                    TimeLineData(
                                        it.fishType,
                                        it.fishLength,
                                        getFeedTimeFormat(it.date)
                                    )
                                }
                            )
                        )
                    }
                    false -> {
                        val typeInfo = feed.typeInfo
                        val photo = feed.fishingRecordList[0]

                        list.add(
                            FeedPhotoData(
                                typeInfo.userName,
                                typeInfo.location,
                                getFeedDateFormat(typeInfo.id.toDate()),
                                photo.imageUrl,
                                photo.fishType,
                                photo.fishLength
                            )
                        )
                    }
                }
            }

            CoroutineScope(Dispatchers.IO + NonCancellable).launch {
                saveFeedList(feedList)
            }
        } else {
            //todo
        }

        return list
    }

    private suspend fun saveFeedList(feedList: List<FeedDTO>) {
        feedList.forEach { feed ->
            localDataSource.saveFeed(
                feed.typeInfo.run {
                    TypeInfoCached(id.toDate(), isTimeline, location, fishingTime, userName)
                },

                feed.fishingRecordList.map {
                    FishingRecordCached(
                        feed.typeInfo.id.toDate(),
                        it.id,
                        it.imageUrl,
                        it.date,
                        it.fishLength,
                        it.fishType
                    )
                }
            )
        }
    }
}
