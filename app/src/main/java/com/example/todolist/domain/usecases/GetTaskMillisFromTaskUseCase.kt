package com.example.todolist.domain.usecases

import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.ApplicationTimeAndDateFormat
import java.util.Date

class GetTaskMillisFromTaskUseCase(
    private val selectedDate: Date?,
    private val selectedTimeInString: String?,
) {

    private val dateFormat = ApplicationDateFormat.access()
    private val dateAndTimeFormat = ApplicationTimeAndDateFormat.access()

    operator fun invoke(): Long {

        return if (selectedDate != null) {
            val selectedDateInString = dateFormat.format(selectedDate)
            val selectedFullDateInString = "$selectedDateInString ${selectedTimeInString ?: ""}"

            val selectedFullDateMillis = dateAndTimeFormat.parse(selectedFullDateInString)

            selectedFullDateMillis!!.time

        } else 0
    }
}