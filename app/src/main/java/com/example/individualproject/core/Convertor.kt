package com.example.individualproject.core

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Convertor {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let {
        LocalDateTime.parse(it)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? =
        date?.toString()
}