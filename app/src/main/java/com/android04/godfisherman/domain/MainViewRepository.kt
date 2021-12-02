package com.android04.godfisherman.domain

import android.location.Location
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord

interface MainViewRepository {

    suspend fun saveTimeLineRecord(time: Double): Result<Boolean>
    suspend fun loadTmpTimeLineRecord(): List<TmpFishingRecord>
    fun saveLocation(location: Location?)

}