package com.android04.godfisherman.ui.feed

import android.util.Log
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
    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

    val isLoading: LiveData<Boolean> get() = _isLoading

    var pagingData : Flow<PagingData<FeedData>> = repository.getFeedDataList(Type.ALL).cachedIn(viewModelScope)

    fun fetchFeedDataList(type: Type): Flow<PagingData<FeedData>> {
        pagingData = repository.getFeedDataList(type).cachedIn(viewModelScope)
        Log.d("isLoading1", "$_isLoading")
        return pagingData
    }

    fun setFilter(checkedId: Int): Flow<PagingData<FeedData>> {
        _isLoading.postValue(true)
        Log.d("isLoading2", "$_isLoading")
        return when (checkedId) {
            R.id.cp_type_photo -> {
                _isLoading.postValue(false)
                fetchFeedDataList(Type.PHOTO)
            }
            R.id.cp_type_timeline -> {
                _isLoading.postValue(false)
                fetchFeedDataList(Type.TIMELINE)
            }
            else -> {
                _isLoading.postValue(false)
                fetchFeedDataList(Type.ALL)
            }
        }
    }

}