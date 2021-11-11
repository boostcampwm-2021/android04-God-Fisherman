package com.android04.godfisherman.ui.stopwatch

import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.StopwatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StopWatchInfoTestViewModel  @Inject constructor(
    private val repository: StopwatchRepository
) : ViewModel() {

}
