package com.android04.godfisherman.data.entity

data class Type(
    @field:JvmField
    val isTimeline: Boolean,
    val location: String,
    val fishingTime: Int,
    val userName: String
)