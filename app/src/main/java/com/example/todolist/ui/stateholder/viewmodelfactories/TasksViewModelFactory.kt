package com.example.todolist.ui.stateholder.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel

class TasksViewModelFactory(
    private val tasksRoomDatabaseRepositoryInterface: TasksRepositoryInterface
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(tasksRoomDatabaseRepositoryInterface) as T
        }

        throw IllegalArgumentException("IllegalArgumentException thrown by TasksRoomDatabaseViewModelFactory")
    }
}