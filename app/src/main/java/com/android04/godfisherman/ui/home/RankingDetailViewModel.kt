package com.android04.godfisherman.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _rankList: MutableLiveData<List<List<RankingData>>> by lazy {
        MutableLiveData<List<List<RankingData>>>()
    }
    val rankList: LiveData<List<List<RankingData>>> = _rankList

    fun fetchRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            val diferredSizeRanking = async {
                homeRepository.fetchRankingList()
            }
            val diferredTimeRanking = async {
                homeRepository.fetchWaitingRankingList()
            }

            val sizeRanking = diferredSizeRanking.await()
            val timeRanking = diferredTimeRanking.await()
            val ranking = listOf(sizeRanking, timeRanking)
            _rankList.postValue(ranking)
        }
    }
}
