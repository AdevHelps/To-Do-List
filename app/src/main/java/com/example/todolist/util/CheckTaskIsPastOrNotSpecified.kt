package com.example.todolist.util

import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.ApplicationTimeAndDateFormat
import java.util.Date

class CheckTaskIsPastOrNotSpecified {

    private val dateFormat = ApplicationDateFormat.access()
    private val timeAndDateFormat = ApplicationTimeAndDateFormat.access()

    fun check(taskDate: Date?, taskTimeInString: String?): Boolean {
        return if (taskDate == null) { true }
        else {

            val currentDateInMillis = Date().time

            val taskDateInString = dateFormat.format(taskDate)

            return if (taskDateInString.isNotEmpty()) {
                val fullDateInString = if (taskTimeInString.isNullOrEmpty()) {
                    "$taskDateInString 00:00 AM"
                } else {
                    "$taskDateInString $taskTimeInString"
                }
                val taskFullDateInMillis =
                    timeAndDateFormat.parse(fullDateInString)?.time ?: return false

                currentDateInMillis >= taskFullDateInMillis
            } else true
        }
    }
}