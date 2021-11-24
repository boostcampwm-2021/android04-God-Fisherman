package com.android04.godfisherman.data.datasource.homedatasource

import com.android04.godfisherman.common.FishRankingRequest
import com.android04.godfisherman.network.response.WeatherResponse
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.ui.home.RankingData

import com.android04.godfisherman.utils.RepoResponse

interface HomeDataSource {
    interface RemoteDataSource {
        suspend fun fetchYoutubeData(callback: RepoResponse<YoutubeResponse?>)
        suspend fun fetchWeatherData(lat: Double, lon: Double, callback: RepoResponse<WeatherResponse?>)
        suspend fun fetchRankingList(request: FishRankingRequest): List<RankingData.HomeRankingData>?
        suspend fun fetchWaitingRankingList(): List<RankingData.HomeWaitingRankingData>
    }
}
