package com.example.todolist.data.broadcastreceivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todolist.R
import com.example.todolist.application.objects.NotificationChannelObject
import com.example.todolist.domain.models.enumclasses.IntentKeys
import com.example.todolist.util.NotificationsUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetUpTasksNotificationBroadcastReceiver: BroadcastReceiver() {

    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var utility: NotificationsUtility

    override fun onReceive(context: Context, intent: Intent) {
        utility.createTasksNotificationChannel(notificationManager)

        val taskID = intent.getIntExtra(IntentKeys.TASK_ID.key, 0)
        val task = intent.getStringExtra(IntentKeys.TASK.key) ?: "No task available"
        val taskDate = intent.getStringExtra(IntentKeys.TASK_DATE.key) ?: ""

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannelObject.channelID
        )
            .setSmallIcon(R.drawable.application_icon)
            .setContentTitle(task)
            .setContentText(taskDate)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationManagerCompat =
                NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(taskID, notification.build())
        }
    }
}