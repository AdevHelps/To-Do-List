package com.example.todolist.data.repositories.implementation

import android.app.Application
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.data.datasources.room.TasksRoomDatabase
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class TasksRepositoryInterfaceImpl @Inject constructor(
    private val applicationContext: Application
): TasksRepositoryInterface {

    private val database by lazy { TasksRoomDatabase.getDatabase(applicationContext) }
    private val tasksRoomDao = database.tasksRoomDao()

    override fun getRowCount(): Int {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getRowCount()
            }
        }
    }

    override fun taskToDao(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            tasksRoomDao.tasksToDb(task)
        }
    }

    override fun getAllTasks(): MutableList<Task> {
        return tasksRoomDao.getAllTasks()
    }

    override fun getOverdueTasksFromDao(todayDate: Long): MutableList<Task> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getOverdueTasks(todayDate)
            }
        }
    }

    override fun getTodayTasksFromDao(todayDate: Date): MutableList<Task> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getTodayTasks(todayDate)
            }
        }
    }

    override fun getTomorrowTasksFromDao(tomorrowDate: Date): MutableList<Task> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getTomorrowTasks(tomorrowDate)
            }
        }
    }

    override fun getLaterTasksFromDao(tomorrowDate: Date): MutableList<Task> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getLaterTasks(tomorrowDate)
            }
        }
    }

    override fun getNoDateTasksFromDao(): MutableList<Task> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getNoDateTasks()
            }
        }
    }

    override fun getLastRowTaskIDFromDao(): Int {
        return runBlocking {
            withContext(Dispatchers.IO) {
                tasksRoomDao.getLastRowTaskId()
            }
        }
    }


    override fun deleteTaskToDao(taskID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            tasksRoomDao.deleteTask(taskID)
        }
    }

    override fun updateTaskToDao(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                tasksRoomDao.updateTask(task)

            }
        }
    }
}