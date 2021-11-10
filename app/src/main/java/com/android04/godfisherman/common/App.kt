package com.android04.godfisherman.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    var exitCameraActivityFlag: Boolean = false
}
