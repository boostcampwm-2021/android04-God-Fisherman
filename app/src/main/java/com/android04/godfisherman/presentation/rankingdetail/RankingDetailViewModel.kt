package com.android04.godfisherman.presentation.rankingdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.di.ApplicationScope
import com.android04.godfisherman.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) : ViewModel() {

    private val _rankList: MutableLiveData<List<RankingPageData>> by lazy {
        MutableLiveData<List<RankingPageData>>()
    }
    val rankList: LiveData<List<RankingPageData>> = _rankList

    fun fetchRanking() {
        externalScope.launch {
            val deferredSizeRanking = async {
                homeRepository.fetchRankingList(FishRankingRequest.DETAIL)
            }
            val deferredTimeRanking = async {
                homeRepository.fetchWaitingRankingList()
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
