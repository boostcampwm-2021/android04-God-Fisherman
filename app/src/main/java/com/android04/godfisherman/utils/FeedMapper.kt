package com.android04.godfisherman.utils

import com.android04.godfisherman.data.dto.FeedDTO
import com.android04.godfisherman.data.dto.FishingRecord
import com.android04.godfisherman.data.dto.TypeInfo
import com.android04.godfisherman.data.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoWithFishingRecords
import com.android04.godfisherman.presentation.feed.FeedPhotoData
import com.android04.godfisherman.presentation.feed.FeedTimelineData
import com.android04.godfisherman.presentation.feed.TimeLineData
import java.text.SimpleDateFormat
import java.util.*

fun TypeInfoWithFishingRecords.toFeedTimeLineData(): FeedTimelineData {
    return FeedTimelineData(
        typeInfo.userName,
        typeInfo.location,
        typeInfo.id.toFeedDateFormat(),
        typeInfo.fishingTime.toTimeHourMinute(),
        fishingRecords.map { it.imageUrl },
        fishingRecords.map {
            TimeLineData(
                it.fishType,
                it.fishLength,
                it.date.toFeedTimeFormat()
            )
        }
    )
}

fun TypeInfoWithFishingRecords.toFeedPhotoData(): FeedPhotoData {
    val photo = fishingRecords[0]

    return FeedPhotoData(
        typeInfo.userName,
        typeInfo.location,
        typeInfo.id.toFeedDateFormat(),
        photo.imageUrl,
        photo.fishType,
        photo.fishLength
    )
}

fun FeedDTO.toFeedTimeLineData(): FeedTimelineData {
    return FeedTimelineData(
        typeInfo.userName,
        typeInfo.location,
        typeInfo.id.toDate().toFeedDateFormat(),
        typeInfo.fishingTime.toTimeHourMinute(),
        fishingRecordList.map { it.imageUrl },
        fishingRecordList.map {
            TimeLineData(
                it.fishType,
                it.fishLength,
                it.date.toFeedTimeFormat()
            )
        }
    )
}

fun FeedDTO.toFeedPhotoData(): FeedPhotoData {
    val photo = fishingRecordList[0]

    return FeedPhotoData(
        typeInfo.userName,
        typeInfo.location,
        typeInfo.id.toDate().toFeedDateFormat(),
        photo.imageUrl,
        photo.fishType,
        photo.fishLength
    )
}

fun TypeInfo.toTypeInfoCached(): TypeInfoCached {
    return TypeInfoCached(id.toDate(), isTimeline, location, fishingTime, userName)
}

fun FishingRecord.toFishingRecordCached(typeInfo: TypeInfo): FishingRecordCached {
    return FishingRecordCached(typeInfo.id.toDate(), id, imageUrl, date, fishLength, fishType)
}

fun Date.toFeedDateFormat(): String {
    val dateFormat = SimpleDateFormat("MM월 dd일")
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    return dateFormat.format(this)
}

fun Date.toFeedTimeFormat(): String {
    val timeFormat = SimpleDateFormat("H:mm")
    timeFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    return timeFormat.format(this)
}
