package com.android04.godfisherman.data.repository

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

    suspend fun fetch(): List<FeedData> {
        val list = mutableListOf<FeedData>()
        remoteDataSource.fetchFeedDataList()?.forEach { feed ->

            when (feed.typeInfo.isTimeline) {
                true -> {
                    val type = feed.typeInfo

                    list.add(
                        FeedTimelineData(
                            type.userName,
                            type.location,
                            type.id.toDate().toString(),
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
                    val type = feed.typeInfo
                    val photo = feed.fishingRecordList[0]

                    list.add(
                        FeedPhotoData(
                            type.userName,
                            type.location,
                            type.id.toDate().toString(),
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