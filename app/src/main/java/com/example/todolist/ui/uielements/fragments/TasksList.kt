package com.example.todolist.ui.uielements.fragments

import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.example.todolist.R
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.data.repositories.interfaces.AppThemeRepositoryInterface
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.databinding.FragmentTasksListBinding
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.domain.models.dataclasses.TaskParcel
import com.example.todolist.domain.models.enumclasses.ThemeStates
import com.example.todolist.domain.usecases.GetTaskMillisFromTaskUseCase
import com.example.todolist.domain.usecases.GetTaskTypeFromDateUseCase
import com.example.todolist.ui.stateholder.viewmodelfactories.TasksViewModelFactory
import com.example.todolist.ui.stateholder.viewmodelfactories.ThemePreferencesViewModelFactory
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel
import com.example.todolist.ui.stateholder.viewmodels.ThemePreferencesViewModel
import com.example.todolist.ui.uielements.activitycontainer.FragmentToActivityContainerInterface
import com.example.todolist.ui.uielements.recyclerviews.adapters.TaskTypesRecyclerViewAdapter
import com.example.todolist.ui.uielements.recyclerviews.TasksRecyclerViewInterface
import com.example.todolist.util.CheckTaskIsPastOrNotSpecifiedUtility
import com.example.todolist.util.NotificationsUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class TasksList: Fragment(R.layout.fragment_tasks_list), TasksRecyclerViewInterface {

    private lateinit var binding: FragmentTasksListBinding
    private lateinit var themePreferencesViewModel: ThemePreferencesViewModel
    private lateinit var tasksViewModel: TasksViewModel
    @Inject lateinit var tasksRepositoryInterface: TasksRepositoryInterface
    @Inject lateinit var getTaskMillisFromTaskUseCase: GetTaskMillisFromTaskUseCase
    @Inject lateinit var getTaskTypeFromDateUseCase: GetTaskTypeFromDateUseCase
    @Inject lateinit var appThemeRepositoryInterface: AppThemeRepositoryInterface
    @Inject lateinit var fragmentToActivityContainerInterface: FragmentToActivityContainerInterface
    @Inject lateinit var alarmManager: AlarmManager
    @Inject lateinit var notificationsUtility: NotificationsUtility
    @Inject lateinit var checkTaskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecifiedUtility
    private val args: TasksListArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tasks_list,
            container,
            false
        )

        val tasksViewModelFactory = TasksViewModelFactory(
            tasksRepositoryInterface,
            getTaskMillisFromTaskUseCase,
            getTaskTypeFromDateUseCase
        )
        tasksViewModel = ViewModelProvider(
            this@TasksList,
            tasksViewModelFactory
        )[TasksViewModel::class.java]

        val themePreferencesViewModelFactory = ThemePreferencesViewModelFactory(appThemeRepositoryInterface)
        themePreferencesViewModel = ViewModelProvider(
            this@TasksList,
            themePreferencesViewModelFactory
        )[ThemePreferencesViewModel::class.java]


        themePreferencesViewModel.checkPrefsExistence()
            .observe(viewLifecycleOwner) { preferencesExist ->

                if (!preferencesExist) {
                    themePreferencesViewModel.createThemePrefs()
                }
            }

        return binding.run {
            tasksListFragmentDataBindingVariable = this@TasksList
            lifecycleOwner = this@TasksList
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            tasksListToolbar.inflateMenu(R.menu.tasks_list_fragment_menu)
            tasksListToolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.lightModeItemID -> {
                        themePreferencesViewModel
                            .passThemeStateToPrefs(ThemeStates.LIGHT.id)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        true
                    }

                    R.id.darkModeItemID -> {
                        themePreferencesViewModel
                            .passThemeStateToPrefs(ThemeStates.DARK.id)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        true
                    }

                    R.id.defaultModeItemID -> {
                        themePreferencesViewModel.clearPrefs()
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        )
                        true
                    }

                    else -> false
                }
            }

            themePreferencesViewModel.getThemeStateFromPrefs()
                .observe(viewLifecycleOwner) { state ->
                when(state) {
                    ThemeStates.LIGHT.id -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    ThemeStates.DARK.id -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }

                    else -> { return@observe }
                }
            }

            tasksViewModel.getRowCount().observe(viewLifecycleOwner) { rowCount ->
                if (rowCount == 0) { noTaskAvailableLL.visibility = View.VISIBLE }
                else { noTaskAvailableLL.visibility = View.GONE }
            }

            val owner = viewLifecycleOwner
            val todayDate = getTodayAndTomorrowDate().first
            val tomorrowDate = getTodayAndTomorrowDate().second

            tasksViewModel.getOverdueTasks(todayDate.time).observe(owner) { overdueTasksPair ->
                    tasksViewModel.getTodayTasks(todayDate).observe(owner) { todayTasksPair ->
                        tasksViewModel.getTomorrowTasks(tomorrowDate)
                            .observe(owner) { tomorrowTasksPair ->
                                tasksViewModel.getLaterTasks(tomorrowDate)
                                    .observe(owner) { laterTasksPair ->
                                        tasksViewModel.getNoDateTasks()
                                            .observe(owner) { noDateTasksPair ->

                                                val adapter = TaskTypesRecyclerViewAdapter(
                                                    checkTaskIsPastOrNotSpecified,
                                                    overdueTasksPair,
                                                    todayTasksPair,
                                                    tomorrowTasksPair,
                                                    laterTasksPair,
                                                    noDateTasksPair,
                                                    this@TasksList,
                                                    requireContext(),
                                                    this@TasksList.tasksViewModel,
                                                    viewLifecycleOwner
                                                )
                                                taskTypesRecyclerViewID.adapter = adapter
                                                taskTypesRecyclerViewID.itemAnimator = null
                                                taskTypesRecyclerViewID.scrollToPosition(
                                                    args.taskType
                                                )
                                            }
                                    }
                            }
                    }
                }
        }
    }

    fun navigateToCreateTaskFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            findNavController().navigate(R.id.toCreateTaskFragment)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                findNavController().navigate(R.id.toCreateTaskFragment)

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    permissionsDialog(
                        "This permission is needed to be notified about your tasks when they arrive",
                        "GRANT"
                    )
                } else {
                    permissionsDialog(
                        "You can grant the permission in settings",
                        ""
                    )
                }
            }
        }

    private fun permissionsDialog(message: String, positiveButtonMessage: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.customAlertDialog)
            .setMessage(message)
            .setPositiveButton(positiveButtonMessage) { _, _ ->

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("DENY") { _, _ ->

            }
            .show()
    }

    private fun getTodayAndTomorrowDate(): Pair<Date, Date> {
        val dateFormat = ApplicationDateFormat.access()

        val currentDate = Date()
        val currentDateFormatted = dateFormat.format(currentDate)
        val todayDate = dateFormat.parse(currentDateFormatted)!!

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrowStringDate = dateFormat.format(calendar.time)
        val tomorrowDate = dateFormat.parse(tomorrowStringDate)!!

        return Pair(todayDate, tomorrowDate)
    }

    override fun onCheckBoxChecked(
        taskListSize: Int,
        taskID: Int,
        checkBox: CheckBox,
        notifyRemoved: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(requireContext(), R.style.customAlertDialog)
            .setMessage("Remove task?")
            .setPositiveButton("CANCEL") { _, _ ->

                checkBox.isChecked = false
            }
            .setNegativeButton("REMOVE") { _, _ ->

                tasksViewModel.deleteTask(taskID)
                notifyRemoved.invoke()

                notificationsUtility.cancelSchedulingTask(alarmManager, requireContext(), taskID)

                notificationsUtility.cancelNotification(requireContext(), taskID)

                fragmentToActivityContainerInterface.showTaskSnackBar(
                    binding.tasksListFragmentContainerCL,
                    "Task removed"
                )

                tasksViewModel.listIsEmptyLiveData.value = taskListSize == 1

                tasksViewModel.getRowCount().observe(viewLifecycleOwner) { rowCount ->
                    binding.apply {
                        if (rowCount == 0) { noTaskAvailableLL.visibility = View.VISIBLE }
                        else { noTaskAvailableLL.visibility = View.GONE }
                    }
                }
            }
            .setCancelable(false)
            .show()
    }

    override fun onClickAction(taskPosition: Int, task: Task) {
        val taskParcel = TaskParcel(
            taskPosition,
            task.taskID,
            task.task,
            task.taskDate,
            task.taskTimeInSeconds,
            task.taskTimeInString,
        )

        val action = TasksListDirections.toEditTaskFragment(taskParcel)
        findNavController().navigate(action)
    }
}