package com.example.todolist.domain.models.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TaskParcel(

    val position: Int,
    val taskID: Int,
    val task: String,
    val taskDate: Date?,
    val taskTimeInSeconds: Int,
    val taskTimeInString: String?,

): Parcelable