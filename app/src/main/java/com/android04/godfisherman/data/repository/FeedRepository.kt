package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.ui.feed.FeedData
import com.android04.godfisherman.ui.feed.FeedPhotoData
import com.android04.godfisherman.ui.feed.FeedTimelineData
import com.android04.godfisherman.ui.feed.TimeLineData
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource
) {

    suspend fun fetch(type: Type): List<FeedData> {
        val list = mutableListOf<FeedData>()
        remoteDataSource.fetchFeedDataList(type)?.forEach { feed ->

            when (feed.typeInfo.isTimeline) {
                true -> {
                    val typeInfo = feed.typeInfo

                    list.add(
                        FeedTimelineData(
                            typeInfo.userName,
                            typeInfo.location,
                            typeInfo.id.toDate().toString(),
                            feed.fishingRecordList.map { it.imageUrl },
                            feed.fishingRecordList.map {
                                TimeLineData(
                                    it.fishType,
                                    it.fishLength,
                                    it.date.toString()
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
                            typeInfo.id.toDate().toString(),
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
}