package com.android04.godfisherman.data.datasource.homedatasource

import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.ui.home.RankingData

import com.android04.godfisherman.utils.RepoResponse

interface HomeDataSource {
    interface RemoteDataSource {
        suspend fun fetchYoutubeData(callback: RepoResponse<YoutubeResponse?>)
        suspend fun fetchRankingList(num: Long): List<RankingData.HomeRankingData>
        suspend fun fetchWaitingRankingList(): List<RankingData.HomeWaitingRankingData>
    }
}
