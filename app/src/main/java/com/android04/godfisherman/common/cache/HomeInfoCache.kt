package com.android04.godfisherman.common.cache

import com.android04.godfisherman.presentation.home.HomeRecommendData
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import javax.inject.Singleton

@Singleton
class HomeInfoCache {
    private var rankingList: List<RankingData.HomeRankingData>? = null
    private var youtubeList: List<HomeRecommendData>? = null

    fun getRankingList(): List<RankingData.HomeRankingData>? = rankingList

    fun putRankingList(list: List<RankingData.HomeRankingData>) {
        rankingList = list
    }

    fun getYoutubeList(): List<HomeRecommendData>? = youtubeList

    fun putYoutubeList(list: List<HomeRecommendData>) {
        youtubeList = list
    }
}
