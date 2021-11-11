package com.android04.godfisherman.ui.feed

import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: FeedRepository) : ViewModel() {

}