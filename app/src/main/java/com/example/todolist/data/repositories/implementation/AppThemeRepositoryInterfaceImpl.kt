package com.example.todolist.data.repositories.implementation

import android.app.Application
import com.example.todolist.data.datasources.DarkModePreferences
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import javax.inject.Inject

class AppThemeRepositoryInterfaceImpl @Inject constructor(
    applicationContext: Application
): AppThemeRepositoryInterface {

    private val darkModeStatePreferences = DarkModePreferences(applicationContext)


    override fun checkIfPreferencesExist(): Boolean {
        return darkModeStatePreferences.checkIfPreferencesExist()
    }

    override fun createDarkModePreferences() {
        darkModeStatePreferences.createDarkModePreferences()
    }

    override fun passDarkModeStateToPreferences(state: Int) {
        darkModeStatePreferences.passDarkModeStateToPreferences(state)
    }


    override fun getDarkModeStateFromPreferences(): Int {
        return darkModeStatePreferences.getDarkModeStateFromPreferences()
    }
}