package com.example.todolist.data.datasources.room

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.example.todolist.application.objects.ApplicationDateFormat
import java.util.Date

class DateToMillisTypeConverter {

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return if (timestamp != null) {

            val date = Date(timestamp)
            val dateFormat = ApplicationDateFormat.access()
            val stringDate = dateFormat.format(date)
            dateFormat.parse(stringDate)
        }
        else null
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}