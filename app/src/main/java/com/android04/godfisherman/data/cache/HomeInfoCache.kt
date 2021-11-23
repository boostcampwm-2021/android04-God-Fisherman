package com.android04.godfisherman.data.cache

import com.android04.godfisherman.ui.home.HomeRecommendData
import javax.inject.Singleton

@Singleton
class HomeInfoCache {
    private var youtubeList: List<HomeRecommendData>? = null

    fun getYoutubeList(): List<HomeRecommendData>? = youtubeList

    fun putYoutubeList(list: List<HomeRecommendData>) {
        youtubeList = list
    }
}
