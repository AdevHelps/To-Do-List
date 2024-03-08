package com.example.todolist.data.datasources

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.domain.models.enumclasses.ThemeStates

class DarkModePreferences(private val context: Context) {

    private val preferencesName = "switcherStateToSharedPreferences"

    fun checkIfPreferencesExist(): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.all.isNotEmpty()
    }

    fun createDarkModePreferences(): SharedPreferences {
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    fun passDarkModeStateToPreferences(state: Int) {
        val sharedPreferencesEditor = createDarkModePreferences().edit()
        sharedPreferencesEditor.putInt("switcherState", state).apply()
    }

    fun getDarkModeStateFromPreferences(): Int {
        val sharedPreferences = context.getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt("switcherState", ThemeStates.DEFAULT.id)
    }
}