package com.android04.godfisherman.presentation.rankingdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.common.di.IoDispatcher
import com.android04.godfisherman.data.repository.HomeInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailViewModel @Inject constructor(
    private val homeInfoRepository: HomeInfoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _rankList: MutableLiveData<List<RankingPageData>> by lazy {
        MutableLiveData<List<RankingPageData>>()
    }
    val rankList: LiveData<List<RankingPageData>> = _rankList

    fun fetchRanking() {
        viewModelScope.launch(ioDispatcher) {
            val deferredSizeRanking = async {
                homeInfoRepository.fetchRankingList(FishRankingRequest.DETAIL)
            }
            val deferredTimeRanking = async {
                homeInfoRepository.fetchWaitingRankingList()
            }

            val sizeRanking = deferredSizeRanking.await()
            val timeRanking = deferredTimeRanking.await()

            if (sizeRanking is Result.Success) {
                val ranking = listOf(
                    RankingPageData(RankingType.SIZE, sizeRanking.data),
                    RankingPageData(RankingType.TIME, timeRanking)
                )
                _rankList.postValue(ranking)
            }
        }
    }
}
