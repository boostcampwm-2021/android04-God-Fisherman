package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.RepoResponseImpl
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.cache.HomeInfoCache
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.network.response.WeatherResponse
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.presentation.home.HomeCurrentWeather
import com.android04.godfisherman.presentation.home.HomeDetailWeather
import com.android04.godfisherman.presentation.home.HomeRecommendData
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import com.android04.godfisherman.utils.roundTime
import com.android04.godfisherman.utils.timeConvertUTC
import com.android04.godfisherman.utils.timeConvertUTCFull
import javax.inject.Inject
import kotlin.math.roundToInt

class HomeInfoRepository @Inject constructor(
    private val remoteDataSource: HomeDataSource.RemoteDataSource,
    private val homeInfoCache: HomeInfoCache
) {
    suspend fun fetchYoutubeData(repoCallback: RepoResponseImpl<List<HomeRecommendData>>, isRefresh: Boolean) {
        val cached = homeInfoCache.getYoutubeList()

        if (!isRefresh && cached != null) {
            repoCallback.invoke(true, cached)
            return
        }

        val callback = RepoResponseImpl<YoutubeResponse?>()

        callback.addSuccessCallback {
            val list = it?.items?.map {
                val snippet = it.snippet
                val url = YOUTUBE_VIDEO_URL + it.id.videoId
                HomeRecommendData(snippet.thumbnails.high.url, snippet.title, url)
            } ?: listOf()

            homeInfoCache.putYoutubeList(list)
            repoCallback.invoke(true, list)
        }

        callback.addFailureCallback {
            repoCallback.invoke(false, listOf())
        }

        remoteDataSource.fetchYoutubeData(callback)
    }

    suspend fun fetchWeatherData(
        lat: Double,
        lon: Double,
        currentCallback: RepoResponse<HomeCurrentWeather?>,
        detailCallback: RepoResponse<List<HomeDetailWeather>?>
    ) {
        val callback = RepoResponseImpl<WeatherResponse?>()

        callback.addSuccessCallback {
            if (it != null) {
                val time = timeConvertUTCFull(it.current.dt)
                val sunrise = timeConvertUTC(it.current.sunrise)
                val sunset = timeConvertUTC(it.current.sunset)
                val temp = ((it.current.temp * 10).roundToInt() / 10F).toString() + "°C"
                val desc = it.current.weather.first().description
                val icon = it.current.weather.first().icon
                val iconUrl = getWeatherIconUrl(icon)

                currentCallback.invoke(
                    true, HomeCurrentWeather(
                        time, sunrise, sunset, temp, desc, iconUrl
                    )
                )

                val list = it.hourly.subList(0, 24).map {
                    val hour = roundTime(timeConvertUTC(it.dt))
                    val hourTemp = it.temp.roundToInt().toString() + "°C"
                    val hourIcon = it.weather.first().icon
                    val hourUrl = getWeatherIconUrl(hourIcon)

                    HomeDetailWeather(hour, hourTemp, hourUrl)
                }

                detailCallback.invoke(true, list)
            }
        }

        callback.addFailureCallback {
            currentCallback.invoke(false, null)
            detailCallback.invoke(true, null)
        }

        remoteDataSource.fetchWeatherData(lat, lon, callback)
    }

    suspend fun fetchRankingList(request: FishRankingRequest, isRefresh: Boolean = false): Result<List<RankingData.HomeRankingData>> {
        if (request == FishRankingRequest.HOME) {
            val cached = homeInfoCache.getRankingList()
            if (!isRefresh && cached != null) {
                return Result.Success(cached)
            }
        }

        val rankingList = remoteDataSource.fetchRankingList(request)

        return if (rankingList != null) {
            if (request == FishRankingRequest.HOME) {
                homeInfoCache.putRankingList(rankingList)
            }

            Result.Success(rankingList)
        } else {
            Result.Fail("랭킹 정보 요청 실패")
        }
    }

    suspend fun fetchWaitingRankingList(): List<RankingData.HomeWaitingRankingData> =
        remoteDataSource.fetchWaitingRankingList()

    private fun getWeatherIconUrl(icon: String) =
        WEATHER_IMAGE_URL_PREFIX + icon + WEATHER_IMAGE_URL_SUFFIX

    companion object {
        const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
        const val WEATHER_IMAGE_URL_PREFIX = "http://openweathermap.org/img/wn/"
        const val WEATHER_IMAGE_URL_SUFFIX = "@2x.png"
    }
}
