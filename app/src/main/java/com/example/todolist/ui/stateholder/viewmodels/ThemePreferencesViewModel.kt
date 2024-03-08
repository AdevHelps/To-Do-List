package com.example.todolist.ui.stateholder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemePreferencesViewModel @Inject constructor(
    private val appThemeRepositoryInterface: AppThemeRepositoryInterface
): ViewModel() {

    fun checkIfPreferencesExist(): MutableLiveData<Boolean> {
        val preferencesExistLiveData = MutableLiveData<Boolean>()
        preferencesExistLiveData.value = appThemeRepositoryInterface.checkIfPreferencesExist()
        return preferencesExistLiveData
    }

    fun createDarkModePreferences() {
        appThemeRepositoryInterface.createDarkModePreferences()
    }

    fun passDarkModeStateToPreferences(state: Int) {
        appThemeRepositoryInterface.passDarkModeStateToPreferences(state)
    }

    fun getDarkModePreferences(): MutableLiveData<Int> {
        val darkModeStateLiveData = MutableLiveData<Int>()
        darkModeStateLiveData.value = appThemeRepositoryInterface.getDarkModeStateFromPreferences()
        return darkModeStateLiveData
    }
}