package com.example.todolist.application.objects

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object ApplicationTimeAndDateFormat {

    @SuppressLint("SimpleDateFormat")
    fun access(): SimpleDateFormat {
        return SimpleDateFormat("EEE, MMM dd, yyyy hh:mm a")
    }
}