package com.android04.godfisherman.data.entity

import com.google.firebase.Timestamp

data class Type(
    val id: Timestamp,
    @field:JvmField
    val isTimeline: Boolean,
    val location: String,
    val fishingTime: Int,
    val userName: String
)