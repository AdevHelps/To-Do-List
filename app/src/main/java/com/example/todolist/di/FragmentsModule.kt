package com.example.todolist.di

import com.example.todolist.ui.uielements.FragmentsUtility
import com.example.todolist.ui.uielements.fragments.EditTaskFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentsModule {

    @Provides
    fun provideInsertionRulesUtility(): FragmentsUtility {
        return FragmentsUtility()
    }
}