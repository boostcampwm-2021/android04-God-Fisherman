package com.android04.godfisherman.data.entity

import com.google.firebase.Timestamp

data class Type(
    val id: Timestamp = Timestamp.now(),
    @field:JvmField
    val isTimeline: Boolean = false,
    val location: String = "",
    val fishingTime: Int = 0,
    val userName: String = ""
)