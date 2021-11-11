package com.android04.godfisherman.data.entity

import java.util.*

data class FishingRecord(
    val id: Int,
    val imageUrl: String,
    val date: Date,
    val fishLength: Double,
    val fishType: String
)
