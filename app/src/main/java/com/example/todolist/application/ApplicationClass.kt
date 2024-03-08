package com.example.todolist.application

import android.app.Application
import android.app.NotificationManager
import com.example.todolist.util.NotificationsUtility
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {

    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var notificationsUtility: NotificationsUtility

    override fun onCreate() {
        super.onCreate()

        notificationsUtility.createTasksNotificationChannel(notificationManager)
    }
}