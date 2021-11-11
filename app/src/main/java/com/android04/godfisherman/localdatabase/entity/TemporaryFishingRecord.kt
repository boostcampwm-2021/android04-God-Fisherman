package com.android04.godfisherman.localdatabase.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "temporary_fishing_record")
data class TemporaryFishingRecord(
    val image: Bitmap,
    val date: Date,
    val fishLength: Double,
    val fishType: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
