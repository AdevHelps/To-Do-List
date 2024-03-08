package com.example.todolist.data.datasources.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolist.domain.models.dataclasses.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateToMillisTypeConverter::class)
abstract class TasksRoomDatabase: RoomDatabase() {

    abstract fun tasksRoomDao(): TasksRoomDao

    companion object {
        @Volatile
        private var INSTANCE: TasksRoomDatabase? = null

        fun getDatabase(context: Context): TasksRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksRoomDatabase::class.java,
                    "tasks_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

