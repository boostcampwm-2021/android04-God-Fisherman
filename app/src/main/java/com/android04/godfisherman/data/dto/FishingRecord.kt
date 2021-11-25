package com.android04.godfisherman.data.dto

import java.util.*

data class FishingRecord(
    val id: Int = -1,
    val imageUrl: String = "",
    val date: Date = Date(),
    val fishLength: Double = 1.1,
    val fishType: String = ""
)
