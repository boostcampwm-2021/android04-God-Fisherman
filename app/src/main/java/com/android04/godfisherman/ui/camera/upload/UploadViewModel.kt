package com.android04.godfisherman.ui.camera.upload

import androidx.lifecycle.*
import com.android04.godfisherman.data.repository.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) : ViewModel() {
    private val _fishTypeList: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    val fishTypeList: LiveData<List<String>> = _fishTypeList

    fun fetchFishTypeList() {
        viewModelScope.launch {
            _fishTypeList.value = repository.fetchFishTypeList()
        }
    }
}