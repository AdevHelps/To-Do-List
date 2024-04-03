package com.example.todolist.data.repositories.interfaces

interface AppThemeRepositoryInterface {

    fun checkPrefsExistence(): Boolean

    fun createThemePrefs()

    fun passThemeStateToPrefs(state: Int)

    fun getThemeStateFromPrefs(): Int

    fun clearPrefs()
}