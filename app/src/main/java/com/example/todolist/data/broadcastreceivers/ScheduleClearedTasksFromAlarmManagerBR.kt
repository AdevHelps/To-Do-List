package com.example.todolist.data.broadcastreceivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.ApplicationTimeAndDateFormat
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.util.NotificationsUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleClearedTasksFromAlarmManagerBR: BroadcastReceiver() {

    @Inject lateinit var tasksRepositoryInterface: TasksRepositoryInterface
    @Inject lateinit var notificationsUtility: NotificationsUtility

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            CoroutineScope(Dispatchers.IO).launch {
                val tasks = tasksRepositoryInterface.getAllTasks()
                scheduleAllTasks(context, tasks)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun scheduleAllTasks(context: Context, tasksList: List<Task>) {
        val dateFormat = ApplicationDateFormat.access()
        val timeAndDateFormat = ApplicationTimeAndDateFormat.access()

        tasksList.forEach { task ->
            val taskDate = dateFormat.format(task.taskDate!!)
            val taskTimeAndDateInString = "$taskDate ${task.taskTimeInString}"
            val taskTimeAndDate = timeAndDateFormat.parse(taskTimeAndDateInString)!!
            val taskFullDateInMillis = taskTimeAndDate.time

            notificationsUtility.scheduleTask(
                context,
                task.task,
                task.taskDate,
                task.taskTimeInString,
                taskFullDateInMillis,
                task.taskID
            )
        }
    }
}