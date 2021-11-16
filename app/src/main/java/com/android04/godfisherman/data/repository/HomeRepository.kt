package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.data.entity.Rank
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.ui.home.HomeRecommendData
import com.android04.godfisherman.utils.RepoResponseImpl
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDataSource: HomeDataSource.RemoteDataSource
) {
    suspend fun fetchYoutubeData(repoCallback: RepoResponseImpl<List<HomeRecommendData>>) {
        val callback = RepoResponseImpl<YoutubeResponse?>()

        callback.addSuccessCallback {
            val list = it?.items?.map {
                val snippet = it.snippet
                val url = YOUTUBE_VIDEO_URL + it.id.videoId
                HomeRecommendData(snippet.thumbnails.high.url, snippet.title, url)
            } ?: listOf()
            repoCallback.invoke(true, list)
        }

        callback.addFailureCallback {
            repoCallback.invoke(false, listOf())
        }

        remoteDataSource.fetchYoutubeData(callback)
    }

    suspend fun fetchRankingList(): List<Rank> = remoteDataSource.fetchRankingList(10)

    companion object {
        const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
    }
}
