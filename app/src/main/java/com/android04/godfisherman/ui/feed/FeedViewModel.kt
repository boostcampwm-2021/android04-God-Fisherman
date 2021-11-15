package com.android04.godfisherman.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: FeedRepository) : ViewModel() {
    private val _feedDataList: MutableLiveData<List<FeedData>> by lazy { MutableLiveData<List<FeedData>>() }

    val feedDataList: LiveData<List<FeedData>> get() = _feedDataList

    fun fetchFeedDataList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _feedDataList.postValue(repository.fetch())
            }
        }
    }
}