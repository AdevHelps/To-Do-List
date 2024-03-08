package com.example.todolist.data.repositories.interfaces

interface AppThemeRepositoryInterface {

    fun checkIfPreferencesExist(): Boolean

    fun createDarkModePreferences()

    fun passDarkModeStateToPreferences(state: Int)

    fun getDarkModeStateFromPreferences(): Int
}