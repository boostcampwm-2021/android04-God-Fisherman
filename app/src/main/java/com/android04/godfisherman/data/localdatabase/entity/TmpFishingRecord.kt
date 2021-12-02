package com.android04.godfisherman.data.localdatabase.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "temporary_fishing_record")
data class TmpFishingRecord(
    val image: Bitmap,
    val date: Date,
    val fishLength: Double,
    val fishType: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
