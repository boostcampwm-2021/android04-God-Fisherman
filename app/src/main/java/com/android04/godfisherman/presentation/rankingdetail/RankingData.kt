package com.android04.godfisherman.presentation.rankingdetail

sealed class RankingData {
    data class HomeRankingData(
        val userId: String,
        val type: String,
        val size: Double
    ) : RankingData()

    data class HomeWaitingRankingData(
        val userId: String,
        val totalTime: Int
    ) : RankingData()
}

data class RankingPageData(
    val rankingType: RankingType,
    val rankingData: List<RankingData>
)

enum class RankingType { SIZE, TIME }
