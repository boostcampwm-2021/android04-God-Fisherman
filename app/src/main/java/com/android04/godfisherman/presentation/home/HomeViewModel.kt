package com.android04.godfisherman.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.common.Event
import com.android04.godfisherman.common.RepoResponseImpl
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.data.repository.HomeRepository
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.data.repository.LogInRepository
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val logInRepository: LogInRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _address: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: LiveData<String> = _address

    private val _youtubeList: MutableLiveData<List<HomeRecommendData>> by lazy { MutableLiveData<List<HomeRecommendData>>() }
    val youtubeList: LiveData<List<HomeRecommendData>> = _youtubeList

    private val _isYoutubeLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isYoutubeLoading: LiveData<Boolean> = _isYoutubeLoading

    private val _youtubeError: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val youtubeError: LiveData<String> = _youtubeError

    private val _homeCurrentWeather: MutableLiveData<HomeCurrentWeather> by lazy { MutableLiveData<HomeCurrentWeather>() }
    val homeCurrentWeather: LiveData<HomeCurrentWeather> = _homeCurrentWeather

    private val _homeDetailWeather: MutableLiveData<List<HomeDetailWeather>> by lazy { MutableLiveData<List<HomeDetailWeather>>() }
    val homeDetailWeather: LiveData<List<HomeDetailWeather>> = _homeDetailWeather

    private val _isWeatherLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isWeatherLoading: LiveData<Boolean> = _isWeatherLoading

    private val _userName: MutableLiveData<String?> by lazy { MutableLiveData<String?>() }
    val userName: LiveData<String?> = _userName

    private val _rankList: MutableLiveData<List<RankingData.HomeRankingData>> by lazy { MutableLiveData<List<RankingData.HomeRankingData>>() }
    val rankList: LiveData<List<RankingData.HomeRankingData>> = _rankList

    private val _isRankLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isRankLoading: LiveData<Boolean> = _isRankLoading

    private val _error: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }
    val error: LiveData<Event<String>> = _error

    fun fetchRanking(isRefresh: Boolean = false) {
        _isRankLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                homeRepository.fetchRankingList(FishRankingRequest.HOME, isRefresh)) {
                is Result.Success -> {
                    _rankList.postValue(result.data)
                    _isRankLoading.postValue(false)
                }
                is Result.Fail -> {
                    _error.postValue(Event(result.description))
                }
            }
        }
    }

    fun fetchYoutube(isRefresh: Boolean = false) {
        _youtubeError.value = null

        viewModelScope.launch(Dispatchers.IO) {
            _isYoutubeLoading.postValue(true)
            val repoCallback = RepoResponseImpl<List<HomeRecommendData>>()

            repoCallback.addSuccessCallback {
                _youtubeList.postValue(it)
                _isYoutubeLoading.postValue(false)
            }

            repoCallback.addFailureCallback {
                _isYoutubeLoading.postValue(false)
                _youtubeError.postValue("일일 유튜브 API 호출 수를 초과했습니다\n내일 다시 시도해주세요")
            }

            homeRepository.fetchYoutubeData(repoCallback, isRefresh)
        }
    }

    fun fetchWeather() {
        val location = locationRepository.loadLocation()

        _isWeatherLoading.postValue(true)

        if (location != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentCallback = RepoResponseImpl<HomeCurrentWeather?>()

                currentCallback.addSuccessCallback {
                    if (it != null) {
                        _homeCurrentWeather.postValue(it)
                    }
                }

                val detailCallback = RepoResponseImpl<List<HomeDetailWeather>?>()

                detailCallback.addSuccessCallback {
                    if (it != null) {
                        _homeDetailWeather.postValue(it)
                        _isWeatherLoading.postValue(false)
                    }
                }

                detailCallback.addFailureCallback {
                    _isWeatherLoading.postValue(false)
                }

                homeRepository.fetchWeatherData(
                    location.latitude,
                    location.longitude,
                    currentCallback,
                    detailCallback
                )
            }
        }
    }

    fun fetchUserID() {
        _userName.value = logInRepository.getUserInfo().name
    }

    fun loadLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = locationRepository.updateAddress()) {
                is Result.Success -> _address.postValue(result.data)
                is Result.Fail -> _address.postValue(result.description)
            }
        }
    }

}
