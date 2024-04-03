package com.example.todolist.data.datasources

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.domain.models.enumclasses.ThemeStates

class DarkModePreferences(private val context: Context) {

    private val preferencesName = "switcherStateToSharedPreferences"
    private val = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    private val themeStateKey = "themeStateKey"

    fun checkPreferencesExistence(): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.all.isNotEmpty()
    }

    fun createThemePreferences() {
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    fun passThemeStateToPrefs(state: Int) {
        val sharedPreferencesEditor = createThemePreferences().edit()
        sharedPreferencesEditor.putInt(themeStateKey, state).apply()
    }

    fun getThemeStateFromPrefs(): Int {
        val sharedPreferences = context.getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(themeStateKey, ThemeStates.DEFAULT.id)
    }

    fun clearPrefs
}