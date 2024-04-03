package com.example.todolist.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.NotificationChannelObject
import com.example.todolist.domain.models.enumclasses.IntentKeys
import com.example.todolist.data.broadcastreceivers.SetUpTasksNotificationBroadcastReceiver
import java.util.Date
import javax.inject.Inject

class NotificationsUtility @Inject constructor(
    private val alarmManager: AlarmManager,
    private val taskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecifiedUtility
){

    private val dateFormat = ApplicationDateFormat.access()


    fun createTasksNotificationChannel(notificationManager: NotificationManager) {
        val channelID = NotificationChannelObject.channelID
        val channelName = NotificationChannelObject.channelName
        val importance = NotificationChannelObject.importance

        val channel = NotificationChannel(channelID, channelName, importance)
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleTask(
        context: Context,
        task: String,
        taskDate: Date?,
        taskTimeInString: String?,
        taskTimeInMillis: Long,
        taskID: Int
    ) {


        val taskIsPastOrNotSpecified =
            taskIsPastOrNotSpecified.check(taskDate, taskTimeInString)

        if (taskIsPastOrNotSpecified) {
            return
        }

        val taskDateString = taskDate?.let { dateFormat.format(it) } ?: ""

        val intent = Intent(context, SetUpTasksNotificationBroadcastReceiver::class.java)
        intent.putExtra(IntentKeys.TASK_ID.key, taskID)
        intent.putExtra(IntentKeys.TASK.key, task)
        intent.putExtra(IntentKeys.TASK_DATE.key, taskDateString)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskID,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            taskTimeInMillis,
            pendingIntent
        )
    }

    fun rescheduleTask(
        context: Context,
        task: String,
        taskDate: Date?,
        taskTimeInString: String?,
        taskTimeInMillis: Long,
        taskID: Int
    ) {

        val taskIsPastOrNotSpecified =
            taskIsPastOrNotSpecified.check(taskDate, taskTimeInString)

        if (taskIsPastOrNotSpecified) {
            return
        }

        val taskDateString = taskDate?.let { dateFormat.format(it) } ?: ""

        val intent = Intent(context, SetUpTasksNotificationBroadcastReceiver::class.java)
        intent.putExtra(IntentKeys.TASK_ID.key, taskID)
        intent.putExtra(IntentKeys.TASK.key, task)
        intent.putExtra(IntentKeys.TASK_DATE.key, taskDateString)

        val pendingIntent =
            PendingIntent.getBroadcast(context, taskID, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            taskTimeInMillis,
            pendingIntent
        )
    }

    fun cancelSchedulingTask(alarmManager: AlarmManager, context: Context, taskID: Int) {
        val intent = Intent(context, SetUpTasksNotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskID,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun cancelNotification(context: Context, notificationID: Int) {
        NotificationManagerCompat.from(context).cancel(notificationID)
    }
}