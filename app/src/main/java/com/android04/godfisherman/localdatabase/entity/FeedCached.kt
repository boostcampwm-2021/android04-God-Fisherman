package com.android04.godfisherman.localdatabase.entity

import androidx.room.*
import java.util.*

@Entity
data class TypeInfoCached(
    @PrimaryKey val id: Date,
    val isTimeline: Boolean,
    val location: String,
    val fishingTime: Int,
    val userName: String
)


@Entity(
    primaryKeys = ["typeId", "id"],
    foreignKeys = [
        ForeignKey(
            entity = TypeInfoCached::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FishingRecordCached(
    val typeId: Date,
    val id: Int,
    val imageUrl: String,
    val date: Date,
    val fishLength: Double,
    val fishType: String
)

data class TypeInfoWithFishingRecords(
    @Embedded val typeInfo: TypeInfoCached,
    @Relation(
        entity = FishingRecordCached::class,
        parentColumn = "id",
        entityColumn = "typeId"
    )
    val fishingRecords: List<FishingRecordCached>
)