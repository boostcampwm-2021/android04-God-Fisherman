package com.android04.godfisherman.ui.home

sealed class RankingData {
    data class HomeRankingData(
        val userId : String,
        val type : String,
        val size : Double
    ) : RankingData()

    data class HomeWaitingRankingData(
        val userId: String,
        val totalTime: Int
    ) : RankingData()
}
