package com.example.todolist.ui.stateholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.application.objects.ApplicationTimeFormat
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.domain.models.enumclasses.TaskTypes
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.domain.usecases.ConvertStringTimeToSecondsUseCase
import com.example.todolist.domain.usecases.GetTaskMillisFromTaskUseCase
import com.example.todolist.domain.usecases.GetTaskTypeFromDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepositoryInterface: TasksRepositoryInterface
): ViewModel() {
    
    val listIsEmptyLiveData = MutableLiveData<Boolean>()

    fun getRowCount(): MutableLiveData<Int> {
        val rowCountLiveData = MutableLiveData<Int>()
        rowCountLiveData.value = tasksRepositoryInterface.getRowCount()
        return rowCountLiveData
    }

    fun stringTimeToSeconds(string: String?): MutableLiveData<Int> {
        val stringTimeToSecondsLiveData = MutableLiveData<Int>()
        stringTimeToSecondsLiveData.value = ConvertStringTimeToSecondsUseCase(string).invoke()
        return stringTimeToSecondsLiveData
    }

    fun hoursAndMinutesInSecondsToTime(hours: Long, minutes: Long): MutableLiveData<String?> {
        val secondsToTimeLiveData = MutableLiveData<String?>()
        val timeFormat = ApplicationTimeFormat.access()

        val hoursInMillis = hours * 3600000
        val minutesInMillis = minutes * 60000
        val secondsInMillis = hoursInMillis + minutesInMillis

        secondsToTimeLiveData.value = timeFormat.format(secondsInMillis)
        return secondsToTimeLiveData
    }

    fun getTaskMillisFromTask(
        selectedDate: Date?,
        selectedTimeInString: String?)
    : MutableLiveData<Long> {

        val taskMillisLiveData = MutableLiveData<Long>()
        taskMillisLiveData.value = GetTaskMillisFromTaskUseCase(
                selectedDate,
                selectedTimeInString
            ).invoke()
        return taskMillisLiveData
    }

    fun insertTaskToRoom(task: Task) {
        tasksRepositoryInterface.taskToDao(task)
    }

    fun getOverdueTasks(todayDate: Long): MutableLiveData<Pair<String, MutableList<Task>>> {
        val overdueTasks =
            tasksRepositoryInterface.getOverdueTasksFromDao(todayDate)
        val overdueTasksLiveData = MutableLiveData<Pair<String, MutableList<Task>>>()
        overdueTasksLiveData.value = Pair(TaskTypes.OVERDUE.stringName, overdueTasks)
        return overdueTasksLiveData
    }

    fun getTodayTasks(todayDate: Date): MutableLiveData<Pair<String, MutableList<Task>>> {
        val todayTasks =
            tasksRepositoryInterface.getTodayTasksFromDao(todayDate)
        val todayTasksLiveData = MutableLiveData<Pair<String, MutableList<Task>>>()
        todayTasksLiveData.value = Pair(TaskTypes.TODAY.stringName, todayTasks)
        return todayTasksLiveData
    }

    fun getTomorrowTasks(tomorrowDate: Date): MutableLiveData<Pair<String, MutableList<Task>>> {
        val tomorrowTasks =
            tasksRepositoryInterface.getTomorrowTasksFromDao(tomorrowDate)
        val tomorrowTasksLiveData = MutableLiveData<Pair<String, MutableList<Task>>>()
        tomorrowTasksLiveData.value = Pair(TaskTypes.TOMORROW.stringName, tomorrowTasks)
        return tomorrowTasksLiveData
    }

    fun getLaterTasks(tomorrowDate: Date): MutableLiveData<Pair<String, MutableList<Task>>> {
        val laterTasks =
            tasksRepositoryInterface.getLaterTasksFromDao(tomorrowDate)
        val laterTasksLiveData = MutableLiveData<Pair<String, MutableList<Task>>>()
        laterTasksLiveData.value = Pair(TaskTypes.LATER.stringName, laterTasks)
        return laterTasksLiveData
    }

    fun getNoDateTasks(): MutableLiveData<Pair<String, MutableList<Task>>> {
        val noDateTasks = tasksRepositoryInterface.getNoDateTasksFromDao()
        val noDateTasksLiveData = MutableLiveData<Pair<String, MutableList<Task>>>()
        noDateTasksLiveData.value = Pair(TaskTypes.NO_DATE.stringName, noDateTasks)
        return noDateTasksLiveData
    }

    fun getTaskTypeFromDate(taskDate: Date?): MutableLiveData<Int> {
        val taskTypeLiveData = MutableLiveData<Int>()
        taskTypeLiveData.value = GetTaskTypeFromDateUseCase(taskDate).invoke()
        return taskTypeLiveData
    }

    fun getLastRowTaskID(): MutableLiveData<Int> {
        val lastRowTaskIDLiveData = MutableLiveData<Int>()
        lastRowTaskIDLiveData.value = tasksRepositoryInterface.getLastRowTaskIDFromDao()
        return lastRowTaskIDLiveData
    }

    fun deleteTask(taskID: Int) {
        tasksRepositoryInterface.deleteTaskToDao(taskID)
    }

    fun updateTask(task: Task) {
        tasksRepositoryInterface.updateTaskToDao(task)
    }
}