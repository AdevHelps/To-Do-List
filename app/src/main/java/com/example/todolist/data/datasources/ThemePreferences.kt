package com.example.todolist.data.datasources

import android.content.Context
import com.example.todolist.domain.models.enumclasses.ThemeStates

class ThemePreferences(applicationContext: Context) {

    private val preferencesName = "switcherStateToSharedPreferences"
    private val sharedPreferences = applicationContext.getSharedPreferences(
        preferencesName,
        Context.MODE_PRIVATE
    )
    private val themeStateKey = "themeStateKey"

    fun checkPreferencesExistence(): Boolean {
        return sharedPreferences.all.isNotEmpty()
    }

    fun createThemePreferences() {
        sharedPreferences.edit().also {
            it.putInt(themeStateKey, ThemeStates.DEFAULT.id)
            it.apply()
        }
    }

    fun passThemeStateToPrefs(state: Int) {
        sharedPreferences.edit().also {
            it.putInt(themeStateKey, state)
            it.apply()
        }
    }

    fun getThemeStateFromPrefs(): Int {
        return sharedPreferences.getInt(themeStateKey, ThemeStates.DEFAULT.id)
    }

    fun clearPrefs() {
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }
}