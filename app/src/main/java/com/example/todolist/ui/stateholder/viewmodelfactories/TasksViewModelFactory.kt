package com.example.todolist.ui.stateholder.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.domain.usecases.GetTaskMillisFromTaskUseCase
import com.example.todolist.domain.usecases.GetTaskTypeFromDateUseCase
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel

class TasksViewModelFactory(
    private val tasksRepositoryInterface: TasksRepositoryInterface,
    private val getTaskMillisFromTaskUseCase: GetTaskMillisFromTaskUseCase,
    private val getTaskTypeFromDateUseCase: GetTaskTypeFromDateUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(
                tasksRepositoryInterface,
                getTaskMillisFromTaskUseCase,
                getTaskTypeFromDateUseCase
            ) as T
        }

        throw IllegalArgumentException("IllegalArgumentException thrown by TasksViewModelFactory")
    }
}