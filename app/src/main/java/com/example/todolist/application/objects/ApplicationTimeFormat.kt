package com.example.todolist.application.objects

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object ApplicationTimeFormat {

    @SuppressLint("SimpleDateFormat")
    fun access(): SimpleDateFormat {
        return SimpleDateFormat("hh:mm a")
    }
}