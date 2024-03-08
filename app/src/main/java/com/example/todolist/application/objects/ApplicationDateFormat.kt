package com.example.todolist.application.objects

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.TimeZone

object ApplicationDateFormat {

    @SuppressLint("SimpleDateFormat")
    fun access(): SimpleDateFormat {

        val localTimeZone = TimeZone.getDefault()
        val timeZone = TimeZone.getTimeZone(localTimeZone.displayName)

        val dateFormatSDF = SimpleDateFormat("EEE, MMM dd, yyyy")
        dateFormatSDF.timeZone = timeZone
        return dateFormatSDF
    }
}