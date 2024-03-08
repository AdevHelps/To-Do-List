package com.example.todolist.data.datasources.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.domain.models.dataclasses.Task
import java.util.Date

@Dao
interface TasksRoomDao {

    @Insert
    fun tasksToDb(task: Task)

    @Query("SELECT COUNT(*) FROM tasks_database_table")
    fun getRowCount(): Int

    @Query("SELECT * FROM tasks_database_table")
    fun getAllTasks(): MutableList<Task>

    @Query("SELECT * FROM tasks_database_table " +
            "WHERE taskDate < :todayDate " +
            "ORDER BY taskDate ASC, taskTimeInSeconds ASC ")
    fun getOverdueTasks(todayDate: Long): MutableList<Task>


    @Query("SELECT * FROM tasks_database_table " +
            "WHERE taskDate = :todayDate " +
            "ORDER BY taskTimeInSeconds ASC")
    fun getTodayTasks(todayDate: Date): MutableList<Task>

    @Query("SELECT * FROM tasks_database_table " +
            "WHERE taskDate = :tomorrowDate " +
            "ORDER BY taskTimeInSeconds ASC")
    fun getTomorrowTasks(tomorrowDate: Date): MutableList<Task>

    @Query("SELECT * FROM tasks_database_table " +
            "WHERE taskDate > :tomorrowDate " +
            "ORDER BY taskDate ASC, taskTimeInSeconds ASC")
    fun getLaterTasks(tomorrowDate: Date): MutableList<Task>

    @Query("SELECT * FROM tasks_database_table " +
            "WHERE taskDate IS NULL " +
            "ORDER BY task")
    fun getNoDateTasks(): MutableList<Task>

    @Query("SELECT taskID FROM tasks_database_table ORDER BY taskID DESC LIMIT 1")
    fun getLastRowTaskId(): Int

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM tasks_database_table WHERE taskID == :taskID")
    fun deleteTask(taskID: Int)
}