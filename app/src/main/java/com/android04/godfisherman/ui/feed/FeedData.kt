package com.android04.godfisherman.ui.feed

sealed class FeedData {
    abstract val userId: String
    abstract val location: String
    abstract val date: String
}

data class FeedPhotoData(
    override val userId: String,
    override val location: String,
    override val date: String,
    val photoUrl: String,
    val fishType: String,
    val fishSize: Double
) : FeedData()

data class FeedTimelineData(
    override val userId: String,
    override val location: String,
    override val date: String,
    val timeline: List<TimeLineData>,
) : FeedData()

data class TimeLineData(
    val photoUrl: String,
    val fishType: String,
    val fishSize: Double,
    val time: String
)