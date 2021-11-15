package com.android04.godfisherman.data.DTO

import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo

data class FeedDTO(
    val typeInfo: TypeInfo,
    val fishingRecordList: List<FishingRecord>
)
