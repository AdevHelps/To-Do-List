package com.example.todolist.ui.stateholder.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import com.example.todolist.ui.stateholder.viewmodels.ThemePreferencesViewModel

class ThemePreferencesViewModelFactory(
    private val appThemeRepositoryInterface: AppThemeRepositoryInterface
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemePreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemePreferencesViewModel(appThemeRepositoryInterface) as T
        }

        throw IllegalArgumentException("IllegalArgumentException thrown by ThemePreferencesViewModelFactory")
    }
}