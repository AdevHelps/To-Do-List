package com.example.todolist.domain.usecases

import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.domain.models.enumclasses.TaskTypes
import java.util.Calendar
import java.util.Date

class GetTaskTypeFromDateUseCase(private val taskDate: Date?) {

    operator fun invoke(): Int {

        val dateFormat = ApplicationDateFormat.access()

        val currentDate = Date()
        val currentDateFormatted = dateFormat.format(currentDate)
        val todayDate = dateFormat.parse(currentDateFormatted)!!

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrowStringDate = dateFormat.format(calendar.time)
        val tomorrowDate = dateFormat.parse(tomorrowStringDate)!!

        return when {
            taskDate == null -> TaskTypes.NO_DATE.id
            taskDate < todayDate -> TaskTypes.OVERDUE.id
            taskDate  == todayDate -> TaskTypes.TODAY.id
            taskDate == tomorrowDate -> TaskTypes.TOMORROW.id
            taskDate > tomorrowDate -> TaskTypes.LATER.id
            else -> -1
        }
    }
}