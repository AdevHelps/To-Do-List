package com.example.todolist.di

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.ApplicationTimeAndDateFormat
import com.example.todolist.application.objects.ApplicationTimeFormat
import com.example.todolist.util.CheckTaskIsPastOrNotSpecified
import com.example.todolist.util.NotificationsUtility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@SuppressLint("SimpleDateFormat")
object AppModule {

    @Provides
    fun provideCustomDateFormat(): ApplicationDateFormat {
        return ApplicationDateFormat
    }

    @Provides
    fun provideCustomTimeFormat(): ApplicationTimeFormat {
        return ApplicationTimeFormat
    }

    @Provides
    fun provideCustomTimeAndDateFormat(): ApplicationTimeAndDateFormat {
        return ApplicationTimeAndDateFormat
    }

    @Provides
    fun provideNotificationsUtility(
        alarmManager: AlarmManager,
        taskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecified
    ): NotificationsUtility {
        return NotificationsUtility(alarmManager, taskIsPastOrNotSpecified)
    }

    @Provides
    fun provideCheckTaskIsPastOrNotSpecified(): CheckTaskIsPastOrNotSpecified {
        return CheckTaskIsPastOrNotSpecified()
    }

    @Provides
    fun provideNotificationManager(application: Application): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun provideAlarmManager(application: Application): AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}