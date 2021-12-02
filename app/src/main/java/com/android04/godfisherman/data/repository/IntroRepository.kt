package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.SharedPreferenceManager
import com.android04.godfisherman.presentation.intro.IntroViewModel
import javax.inject.Inject

class IntroRepository @Inject constructor(private val manager: SharedPreferenceManager) {
    fun loadFirstStart() =
        manager.getString(IntroViewModel.FIRST_START) != IntroViewModel.FIRST_START

    fun setFirstStart() {
        manager.saveString(IntroViewModel.FIRST_START, IntroViewModel.FIRST_START)
    }
}
