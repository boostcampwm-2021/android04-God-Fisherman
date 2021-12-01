package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.RepoResponseImpl
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.domain.HomeRepository
import com.android04.godfisherman.presentation.home.HomeCurrentWeather
import com.android04.godfisherman.presentation.home.HomeDetailWeather
import com.android04.godfisherman.presentation.home.HomeRecommendData
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeInfoRepository: HomeInfoRepository,
    private val logInRepository: LogInRepository,
    private val locationRepository: LocationRepository
) : HomeRepository {

    override suspend fun fetchYoutubeData(
        repoCallback: RepoResponseImpl<List<HomeRecommendData>>,
        isRefresh: Boolean
    ) = homeInfoRepository.fetchYoutubeData(repoCallback, isRefresh)

    override suspend fun fetchWeatherData(
        lat: Double,
        lon: Double,
        currentCallback: RepoResponse<HomeCurrentWeather?>,
        detailCallback: RepoResponse<List<HomeDetailWeather>?>
    ) = homeInfoRepository.fetchWeatherData(
        lat,
        lon,
        currentCallback,
        detailCallback
    )

    override suspend fun fetchRankingList(
        request: FishRankingRequest,
        isRefresh: Boolean
    ) = homeInfoRepository.fetchRankingList(request, isRefresh)

    override fun getUserInfo() = logInRepository.getUserInfo()

    override suspend fun updateAddress() = locationRepository.updateAddress()

    override fun loadLocation() = locationRepository.loadLocation()

}