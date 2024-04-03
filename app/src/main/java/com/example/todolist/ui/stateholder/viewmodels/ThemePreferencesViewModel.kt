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

    fun checkPrefsExistence(): MutableLiveData<Boolean> {
        val preferencesExistLiveData = MutableLiveData<Boolean>()
        preferencesExistLiveData.value = appThemeRepositoryInterface.checkPrefsExistence()
        return preferencesExistLiveData
    }

    fun createThemePrefs() {
        appThemeRepositoryInterface.createThemePrefs()
    }

    fun passThemeStateToPrefs(state: Int) {
        appThemeRepositoryInterface.passThemeStateToPrefs(state)
    }

    fun getThemeStateFromPrefs(): MutableLiveData<Int> {
        val darkModeStateLiveData = MutableLiveData<Int>()
        darkModeStateLiveData.value = appThemeRepositoryInterface.getThemeStateFromPrefs()
        return darkModeStateLiveData
    }

    fun clearPrefs() {
        appThemeRepositoryInterface.clearPrefs()
    }
}