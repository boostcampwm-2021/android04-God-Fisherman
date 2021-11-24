package com.android04.godfisherman.data.repository

import android.util.Log
import androidx.paging.*
import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.localdatabase.entity.TypeInfoWithFishingRecords
import com.android04.godfisherman.ui.feed.FeedData
import com.android04.godfisherman.utils.*
import com.google.firebase.Timestamp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val localDataSource: FeedDataSource.LocalDataSource,
    private val remoteDataSource: FeedDataSource.RemoteDataSource,
    private val networkChecker: NetworkChecker
) {
    private fun fetchPagingData(type: Type): Flow<PagingData<FeedData>> {
        return Pager(PagingConfig(pageSize = 2)) { FeedRemotePagingSource(type) }.flow
    }

    private fun loadPagingData(type: Type): Flow<PagingData<FeedData>> {
        Log.d("paging3", "loadPagingData")
        return Pager(PagingConfig(pageSize = 2)) { FeedLocalPagingSource(type) }.flow
    }

    fun getFeedDataList(type: Type): Flow<PagingData<FeedData>> {
        return when (networkChecker.isConnected()) {
            true -> fetchPagingData(type)
            false -> loadPagingData(type)
        }
    }

    private fun fetch(feedList: List<FeedDTO>?): List<FeedData> {
        val list = mutableListOf<FeedData>()
        if (feedList != null) {
            feedList.forEach { feed ->
                when (feed.typeInfo.isTimeline) {
                    true -> list.add(feed.toFeedTimeLineData())
                    false -> list.add(feed.toFeedPhotoData())
                }
            }
            CoroutineScope(Dispatchers.IO + NonCancellable).launch {
                saveFeedListInCache(feedList)
            }
        } else {
            //todo
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
        localDataSource.deleteAll()

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
                var response = fetch(feedList)
                LoadResult.Page(
                    data = response,
                    prevKey = null,
                    nextKey = snapshotList?.last()?.get("id") as Timestamp
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
