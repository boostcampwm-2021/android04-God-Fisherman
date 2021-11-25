package com.android04.godfisherman.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android04.godfisherman.R
import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: FeedRepository) : ViewModel() {

    private val _isDataLoading : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }
    val isDataLoading : LiveData<Boolean> = _isDataLoading

    private var pagingData : Flow<PagingData<FeedData>> = repository.getFeedDataList(Type.ALL).cachedIn(viewModelScope)

    private fun fetchFeedDataList(type: Type): Flow<PagingData<FeedData>> {
        pagingData = repository.getFeedDataList(type).cachedIn(viewModelScope)
        return pagingData
    }

    fun setFilter(checkedId: Int): Flow<PagingData<FeedData>> {
        return when (checkedId) {
            R.id.cp_type_photo -> {
                fetchFeedDataList(Type.PHOTO)
            }
            R.id.cp_type_timeline -> {
                fetchFeedDataList(Type.TIMELINE)
            }
            else -> {
                fetchFeedDataList(Type.ALL)
            }
        }
    }

    fun setLoadingOn(){
        _isDataLoading.postValue(true)
    }

    fun setLoadingOff(){
        _isDataLoading.postValue(false)
    }
}