package com.android04.godfisherman.data.repository

import androidx.paging.*
import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.common.constant.Type
import com.android04.godfisherman.data.dto.FeedDTO
import com.android04.godfisherman.data.datasource.feeddatasource.FeedDataSource
import com.android04.godfisherman.data.datasource.feeddatasource.remote.FeedRemoteDataSourceImpl.Companion.FEED_IDENTIFIER_NAME
import com.android04.godfisherman.common.di.ApplicationScope
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoWithFishingRecords
import com.android04.godfisherman.presentation.feed.FeedData
import com.android04.godfisherman.utils.toFeedPhotoData
import com.android04.godfisherman.utils.toFeedTimeLineData
import com.android04.godfisherman.utils.toFishingRecordCached
import com.android04.godfisherman.utils.toTypeInfoCached
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource,
    private val networkChecker: NetworkChecker,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    private fun fetchPagingData(type: Type): Flow<PagingData<FeedData>> {
        if (type == Type.ALL) {
            externalScope.launch {
                localDataSource.deleteAll()
            }
        }

        return Pager(PagingConfig(pageSize = 2)) { FeedRemotePagingSource(type) }.flow
    }

    private fun loadPagingData(type: Type): Flow<PagingData<FeedData>> {
        return Pager(PagingConfig(pageSize = 2)) { FeedLocalPagingSource(type) }.flow
    }

    fun getFeedDataList(type: Type): Flow<PagingData<FeedData>> {
        return when (networkChecker.isConnected()) {
            true -> fetchPagingData(type)
            false -> loadPagingData(type)
        }
    }

    private fun fetchFeedDataList(feedList: List<FeedDTO>?): List<FeedData> {
        val list = mutableListOf<FeedData>()
        if (feedList != null) {
            feedList.forEach { feed ->
                when (feed.typeInfo.isTimeline) {
                    true -> list.add(feed.toFeedTimeLineData())
                    false -> list.add(feed.toFeedPhotoData())
                }
            }
            externalScope.launch {
                saveFeedListInCache(feedList)
            }
        }
        return list
    }


    private suspend fun loadFeedDataList(type: Type, next: Date): List<TypeInfoWithFishingRecords> {
        return localDataSource.loadFeedDataList(type, next)
    }

    private fun feedDataToTypeData(feedData: List<TypeInfoWithFishingRecords>): List<FeedData> {
        val list = mutableListOf<FeedData>()
        feedData.forEach { feed ->
            when (feed.typeInfo.isTimeline) {
                true -> list.add(feed.toFeedTimeLineData())
                false -> list.add(feed.toFeedPhotoData())
            }
        }
        return list
    }

    private suspend fun saveFeedListInCache(feedList: List<FeedDTO>) {
        feedList.forEach { feed ->
            localDataSource.saveFeed(
                feed.typeInfo.toTypeInfoCached(),
                feed.fishingRecordList.map {
                    it.toFishingRecordCached(feed.typeInfo)
                }
            )
        }
    }

    inner class FeedRemotePagingSource(private val type: Type) :
        PagingSource<Timestamp, FeedData>() {
        override val keyReuseSupported = true

        override suspend fun load(params: LoadParams<Timestamp>): LoadResult<Timestamp, FeedData> {
            return try {
                val next = params.key ?: Timestamp.now()
                val snapshotList = remoteDataSource.fetchSnapshotList(type, next)
                val feedList = remoteDataSource.fetchFeedDataList(snapshotList)
                var response = fetchFeedDataList(feedList)
                LoadResult.Page(
                    data = response,
                    prevKey = null,
                    nextKey = snapshotList?.last()?.get(FEED_IDENTIFIER_NAME) as Timestamp
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Timestamp, FeedData>): Timestamp? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(
                    anchorPosition
                )?.prevKey
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey
            }
        }
    }

    inner class FeedLocalPagingSource(private val type: Type) :
        PagingSource<Date, FeedData>() {
        override val keyReuseSupported = true

        override suspend fun load(params: LoadParams<Date>): LoadResult<Date, FeedData> {
            return try {
                val next: Date = params.key ?: Timestamp.now().toDate()
                val feedList = loadFeedDataList(type, next)
                val typeList = feedDataToTypeData(feedList)
                LoadResult.Page(
                    data = typeList, prevKey = null, nextKey = feedList.last().typeInfo.id
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Date, FeedData>): Date? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(
                    anchorPosition
                )?.prevKey
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey
            }
        }
    }
}
