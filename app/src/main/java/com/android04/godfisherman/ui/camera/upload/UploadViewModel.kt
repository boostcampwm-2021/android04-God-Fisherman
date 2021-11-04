package com.android04.godfisherman.ui.camera.upload

import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) : ViewModel() {
}