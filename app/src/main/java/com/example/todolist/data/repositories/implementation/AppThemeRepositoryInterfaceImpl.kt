package com.example.todolist.data.repositories.implementation

import android.app.Application
import com.example.todolist.data.datasources.ThemePreferences
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import javax.inject.Inject

class AppThemeRepositoryInterfaceImpl @Inject constructor(
    applicationContext: Application
): AppThemeRepositoryInterface {

    private val themePreferences = ThemePreferences(applicationContext)


    override fun checkPrefsExistence(): Boolean {
        return themePreferences.checkPreferencesExistence()
    }

    override fun createThemePrefs() {
        themePreferences.createThemePreferences()
    }

    override fun passThemeStateToPrefs(state: Int) {
        themePreferences.passThemeStateToPrefs(state)
    }


    override fun getThemeStateFromPrefs(): Int {
        return themePreferences.getThemeStateFromPrefs()
    }

    override fun clearPrefs() {
        themePreferences.clearPrefs()
    }
}