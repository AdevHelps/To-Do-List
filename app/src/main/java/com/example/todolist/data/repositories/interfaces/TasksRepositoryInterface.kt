package com.example.todolist.data.repositories.interfaces

import com.example.todolist.domain.models.dataclasses.Task
import java.util.Date

interface TasksRepositoryInterface {

    fun getRowCount(): Int

    fun taskToDao(task: Task)

    fun getAllTasks(): MutableList<Task>

    fun getOverdueTasksFromDao(todayDate: Long): MutableList<Task>

    fun getTodayTasksFromDao(todayDate: Date): MutableList<Task>

    fun getTomorrowTasksFromDao(tomorrowDate: Date): MutableList<Task>

    fun getLaterTasksFromDao(tomorrowDate: Date): MutableList<Task>

    fun getNoDateTasksFromDao(): MutableList<Task>

    fun getLastRowTaskIDFromDao(): Int

    fun updateTaskToDao(task: Task)

    fun deleteTaskToDao(taskID: Int)
}