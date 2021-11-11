package com.android04.godfisherman.ui.camera.upload

import android.graphics.Bitmap
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.UploadRepository
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord
import com.android04.godfisherman.ui.camera.CameraActivity
import com.android04.godfisherman.ui.stopwatch.StopwatchViewModel
import com.android04.godfisherman.utils.convertCentiMeter
import com.android04.godfisherman.utils.roundBodySize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) : ViewModel() {
    private val _fishTypeList: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    val fishTypeList: LiveData<List<String>> = _fishTypeList

    val isUploadSuccess = MutableLiveData<Boolean?>(null)
    val isLoading = MutableLiveData<Boolean?>(null)

    var fishTypeSelected: String? = null
    var bodySize: Double? = null
    var bodySizeCentiMeter : String? = null
    lateinit var fishThumbnail: Bitmap

    fun fetchInitData(size: Double) {
        bodySize = roundBodySize(size)
        bodySizeCentiMeter = convertCentiMeter(size)
        fishThumbnail = CameraActivity.captureImage!!
        CameraActivity.captureImage = null
    }

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

    fun saveFishingRecord() {
        if (StopwatchViewModel.isTimeLine){
            if (fishTypeSelected != null && bodySize != null && ::fishThumbnail.isInitialized) {
                isLoading.value = true
                viewModelScope.launch(Dispatchers.IO){
                    repository.saveTmpTimeLineRecord(fishThumbnail,bodySize!!,fishTypeSelected!!)
                    isUploadSuccess.postValue(true)
                    isLoading.postValue(false)
                }
            } else {
                isUploadSuccess.postValue(false)
            }
        } else {
            if (fishTypeSelected != null && bodySize != null && ::fishThumbnail.isInitialized) {
                isLoading.value = true
                viewModelScope.launch {
                    repository.saveImageType(fishThumbnail, bodySize!!, fishTypeSelected!!)
                    isUploadSuccess.postValue(true)
                    isLoading.postValue(false)
                }
            } else {
                isUploadSuccess.postValue(false)
            }

        }
    }
}