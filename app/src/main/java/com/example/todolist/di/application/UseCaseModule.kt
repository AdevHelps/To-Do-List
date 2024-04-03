package com.example.todolist.di.application

import com.example.todolist.domain.usecases.GetTaskMillisFromTaskUseCase
import com.example.todolist.domain.usecases.GetTaskTypeFromDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetTaskMillisFromTaskUseCase(): GetTaskMillisFromTaskUseCase {
        return GetTaskMillisFromTaskUseCase()
    }

    @Provides
    fun provideGetTaskTypeFromDateUseCase(): GetTaskTypeFromDateUseCase {
        return GetTaskTypeFromDateUseCase()
    }
}