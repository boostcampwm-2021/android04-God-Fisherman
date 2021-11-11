package com.android04.godfisherman.localdatabase

import androidx.room.TypeConverter
import java.util.Date

class DatabaseTypeConverter {
    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
