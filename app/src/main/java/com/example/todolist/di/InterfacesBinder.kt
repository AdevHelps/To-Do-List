package com.example.todolist.di

import com.example.todolist.data.repositories.implementation.AppThemeRepositoryInterfaceImpl
import com.example.todolist.data.repositories.implementation.TasksRepositoryInterfaceImpl
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.ui.uielements.activitycontainer.FragmentToActivityContainerInterface
import com.example.todolist.ui.uielements.activitycontainer.MainContainerActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfacesBinder {

    @Binds
    abstract fun bindTasksRepositoryInterface(
        tasksRepositoryImpl: TasksRepositoryInterfaceImpl
    ): TasksRepositoryInterface

    @Binds
    abstract fun bindAppThemeRepositoryInterface(
        appThemeRepositoryImpl: AppThemeRepositoryInterfaceImpl
    ): AppThemeRepositoryInterface

    @Binds
    abstract fun bindFragmentToActivityContainerInterface(
        mainContainerActivity: MainContainerActivity
    ): FragmentToActivityContainerInterface
}