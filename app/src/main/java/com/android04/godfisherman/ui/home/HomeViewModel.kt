package com.android04.godfisherman.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.HomeRepository
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.utils.LocationHelper
import com.android04.godfisherman.utils.RepoResponseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val locationRepository: LocationRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _address: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: LiveData<String> = _address

    private val _youtubeList: MutableLiveData<List<HomeRecommendData>> by lazy { MutableLiveData<List<HomeRecommendData>>() }
    val youtubeList: LiveData<List<HomeRecommendData>> = _youtubeList

    private val _isYoutubeSuccess: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isYoutubeSuccess: LiveData<Boolean> = _isYoutubeSuccess

    private val _rankList: MutableLiveData<List<RankingData.HomeRankingData>> by lazy { MutableLiveData<List<RankingData.HomeRankingData>>() }
    val rankList: LiveData<List<RankingData.HomeRankingData>> = _rankList
    
    fun updateLocation() {
        locationHelper.setLocationUpdate()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val location = locationHelper.getLocation()
                _address.postValue(locationRepository.updateLocation(location))
            }
        }
    }

    fun fetchRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = homeRepository.fetchRankingList()
            _rankList.postValue(list)
        }
    }

    fun fetchYoutube() {
        viewModelScope.launch {
            val repoCallback = RepoResponseImpl<List<HomeRecommendData>>()

            repoCallback.addSuccessCallback {
                _youtubeList.postValue(it)
                _isYoutubeSuccess.postValue(true)
            }

            repoCallback.addFailureCallback {
                _isYoutubeSuccess.postValue(false)
            }

            homeRepository.fetchYoutubeData(repoCallback)
        }
    }
}
