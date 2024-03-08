package com.example.todolist.domain.models.enumclasses

enum class TaskTypes(val id: Int, val stringName: String) {
    NO_DATE(4, "No Date"),
    OVERDUE(0, "Overdue"),
    TODAY(1, "Today"),
    TOMORROW(2, "Tomorrow"),
    LATER(3, "Later")
}