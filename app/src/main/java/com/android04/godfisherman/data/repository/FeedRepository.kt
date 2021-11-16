package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.ui.feed.FeedData
import com.android04.godfisherman.ui.feed.FeedPhotoData
import com.android04.godfisherman.ui.feed.FeedTimelineData
import com.android04.godfisherman.ui.feed.TimeLineData
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource
) {

    suspend fun fetch(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()
        val dateFormat = SimpleDateFormat("MM월 dd일")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        val timeFormat = SimpleDateFormat("HH:mm")
        timeFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

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
                                dateFormat.format(typeInfo.id.toDate()),
                                feed.fishingRecordList.map { it.imageUrl },
                                feed.fishingRecordList.map {
                                    TimeLineData(
                                        it.fishType,
                                        it.fishLength,
                                        timeFormat.format(it.date)
                                    )
                                })
                        )
                    }
                    false -> {
                        val typeInfo = feed.typeInfo
                        val photo = feed.fishingRecordList[0]

                        list.add(
                            FeedPhotoData(
                                typeInfo.userName,
                                typeInfo.location,
                                dateFormat.format(typeInfo.id.toDate()),
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
