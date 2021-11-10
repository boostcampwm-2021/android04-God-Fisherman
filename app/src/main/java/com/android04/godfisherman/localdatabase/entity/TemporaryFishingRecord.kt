package com.android04.godfisherman.localdatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "temporary_fishing_record")
data class TemporaryFishingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val imageUrl: String,
    val date: Date,
    val fishLength: Double,
    val fishType: String
)
