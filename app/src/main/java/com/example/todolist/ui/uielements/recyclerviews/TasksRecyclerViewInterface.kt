package com.example.todolist.ui.uielements.recyclerviews

import android.widget.CheckBox
import com.example.todolist.domain.models.dataclasses.Task

interface TasksRecyclerViewInterface {

    fun onCheckBoxChecked(
        taskListSize: Int,
        taskID: Int,
        checkBox: CheckBox,
        notifyRemoved:() -> Unit
    )

    fun onClickAction(taskPosition: Int, task: Task)
}