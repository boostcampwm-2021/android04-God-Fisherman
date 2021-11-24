package com.android04.godfisherman.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.common.FishRankingRequest
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _rankList: MutableLiveData<List<RankingPageData>> by lazy {
        MutableLiveData<List<RankingPageData>>()
    }
    val rankList: LiveData<List<RankingPageData>> = _rankList

    fun fetchRanking() {
        viewModelScope.launch(Dispatchers.IO) {
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
