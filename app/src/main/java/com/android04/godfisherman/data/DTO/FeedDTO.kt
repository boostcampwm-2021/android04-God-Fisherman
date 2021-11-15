package com.android04.godfisherman.data.DTO

import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type

data class FeedDTO(
    val type: Type,
    val fishingRecordList: List<FishingRecord>
)
