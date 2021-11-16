package com.android04.godfisherman.ui.home

data class HomeRankingData(
    val userId : String,
    val type : String,
    val size : Double
)

data class HomeWaitingRankingData(
    val userId: String,
    val totalTime: Int
)
