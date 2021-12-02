package com.android04.godfisherman.domain

import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.RepoResponseImpl
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.presentation.home.HomeCurrentWeather
import com.android04.godfisherman.presentation.home.HomeDetailWeather
import com.android04.godfisherman.presentation.home.HomeRecommendData
import com.android04.godfisherman.presentation.main.Gps
import com.android04.godfisherman.presentation.mypage.UserInfo
import com.android04.godfisherman.presentation.rankingdetail.RankingData

interface HomeRepository {

    suspend fun fetchYoutubeData(
        repoCallback: RepoResponseImpl<List<HomeRecommendData>>,
        isRefresh: Boolean
    )

    suspend fun fetchWeatherData(
        lat: Double,
        lon: Double,
        currentCallback: RepoResponse<HomeCurrentWeather?>,
        detailCallback: RepoResponse<List<HomeDetailWeather>?>
    )

    suspend fun fetchRankingList(
        request: FishRankingRequest,
        isRefresh: Boolean = false
    ): Result<List<RankingData.HomeRankingData>>

    fun getUserInfo(): UserInfo

    suspend fun updateAddress(): Result<String>

    fun loadLocation(): Gps?

}