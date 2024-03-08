package com.example.todolist.domain.usecases

class ConvertStringTimeToSecondsUseCase(private val stringTime: String?) {

    operator fun invoke(): Int {

        return if (stringTime == "") 0
        else {

            return when {
                stringTime!!.contains("AM") -> {
                    val hours = "${stringTime[0]}${stringTime[1]}".toInt()
                    val minutes = "${stringTime[3]}${stringTime[4]}".toInt()

                    val hoursInSeconds = hours * 3600
                    val minutesInSeconds = minutes * 60
                    hoursInSeconds + minutesInSeconds
                }

                stringTime.contains("PM") -> {
                    val hours = "${stringTime[0]}${stringTime[1]}".toInt() + 12
                    val minutes = "${stringTime[3]}${stringTime[4]}".toInt()

                    val hoursInSeconds = hours * 3600
                    val minutesInSeconds = minutes * 60
                    hoursInSeconds + minutesInSeconds
                }

                else -> {
                    val hours = "${stringTime[0]}${stringTime[1]}".toInt()
                    val minutes = "${stringTime[3]}${stringTime[4]}".toInt()

                    val hoursInSeconds = hours * 3600
                    val minutesInSeconds = minutes * 60
                    hoursInSeconds + minutesInSeconds
                }
            }
        }
    }
}