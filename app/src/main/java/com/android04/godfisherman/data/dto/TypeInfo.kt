package com.android04.godfisherman.data.dto

import com.google.firebase.Timestamp

data class TypeInfo(
    val id: Timestamp = Timestamp.now(),
    @field:JvmField
    val isTimeline: Boolean = false,
    val location: String = "",
    val fishingTime: Int = 0,
    val userName: String = ""
)