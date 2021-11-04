package com.android04.godfisherman.ui.camera.upload

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) : ViewModel() {
    private val _fishTypeList: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    val fishTypeList: LiveData<List<String>> = _fishTypeList

    var fishTypeSelected: String? = null

    fun fetchFishTypeList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _fishTypeList.postValue(repository.fetchFishTypeList())
            }
        }
    }

    fun setFishTypeSelected(selected: Editable) {
        fishTypeSelected = selected.toString()
    }

}