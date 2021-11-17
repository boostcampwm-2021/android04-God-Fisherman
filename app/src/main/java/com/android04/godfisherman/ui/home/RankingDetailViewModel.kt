package com.android04.godfisherman.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _rankList: MutableLiveData<List<RankingData.HomeRankingData>> by lazy { MutableLiveData<List<RankingData.HomeRankingData>>() }
    val rankList: LiveData<List<RankingData.HomeRankingData>> = _rankList

    private val _waitRankList: MutableLiveData<List<RankingData.HomeWaitingRankingData>> by lazy {
        MutableLiveData<List<RankingData.HomeWaitingRankingData>>()
    }
    val waitRankList: LiveData<List<RankingData.HomeWaitingRankingData>> = _waitRankList

    fun fetchRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = homeRepository.fetchRankingList()
            _rankList.postValue(list)
        }
    }

    fun fetchWaitingRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = homeRepository.fetchWaitingRankingList()
            _waitRankList.postValue(list)
        }
    }
}
