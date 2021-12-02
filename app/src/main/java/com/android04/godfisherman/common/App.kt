package com.android04.godfisherman.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    init {
        instance = this
    }

    var exitCameraActivityFlag: Boolean = false

    companion object {
        private lateinit var instance: App
    }
}
