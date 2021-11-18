package com.android04.godfisherman.data.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gps(val longitude: Double, val latitude: Double): Parcelable
