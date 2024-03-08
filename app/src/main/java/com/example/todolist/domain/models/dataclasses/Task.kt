package com.example.todolist.domain.models.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks_database_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val taskID: Int,
    val task: String,
    val taskDate: Date?,
    val taskTimeInSeconds: Int,
    val taskTimeInString: String?,
)