package com.android04.godfisherman.data.repository

import android.location.Location
import com.android04.godfisherman.domain.MainViewRepository
import com.android04.godfisherman.domain.RecordRepository
import javax.inject.Inject
import javax.inject.Singleton

class MainViewRepositoryImpl @Inject constructor(
    private val stopwatchRepository: RecordRepository.StopwatchRepository,
    private val locationRepository: LocationRepository,
) : MainViewRepository {

    override suspend fun saveTimeLineRecord(time: Double) = stopwatchRepository.saveTimeLineRecord(time)

    override suspend fun loadTmpTimeLineRecord() = stopwatchRepository.loadTmpTimeLineRecord()

    override fun saveLocation(location: Location?) = locationRepository.saveLocation(location)

}