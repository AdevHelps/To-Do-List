package com.example.todolist.ui.uielements.fragments

import android.app.AlarmManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.domain.models.dataclasses.TaskParcel
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.domain.usecases.GetTaskMillisFromTaskUseCase
import com.example.todolist.domain.usecases.GetTaskTypeFromDateUseCase
import com.example.todolist.ui.stateholder.viewmodelfactories.TasksViewModelFactory
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel
import com.example.todolist.ui.uielements.activitycontainer.FragmentToActivityContainerInterface
import com.example.todolist.ui.uielements.FragmentsUtility
import com.example.todolist.util.CheckTaskIsPastOrNotSpecifiedUtility
import com.example.todolist.util.NotificationsUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class EditTaskFragment: Fragment(R.layout.fragment_edit_task) {

    private lateinit var binding: FragmentEditTaskBinding
    private lateinit var tasksViewModel: TasksViewModel
    private val args: EditTaskFragmentArgs by navArgs()
    @Inject lateinit var fragmentToActivityContainerInterface: FragmentToActivityContainerInterface
    @Inject lateinit var tasksRepositoryInterface: TasksRepositoryInterface
    @Inject lateinit var getTaskMillisFromTaskUseCase: GetTaskMillisFromTaskUseCase
    @Inject lateinit var getTaskTypeFromDateUseCase: GetTaskTypeFromDateUseCase
    @Inject lateinit var alarmManager: AlarmManager
    @Inject lateinit var notificationsUtility: NotificationsUtility
    @Inject lateinit var fragmentsUtility: FragmentsUtility
    @Inject lateinit var checkTaskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecifiedUtility
    private val dateFormat = ApplicationDateFormat.access()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_task,
            container,
            false
        )

        val tasksViewModelFactory = TasksViewModelFactory(
            tasksRepositoryInterface,
            getTaskMillisFromTaskUseCase,
            getTaskTypeFromDateUseCase
        )
        tasksViewModel = ViewModelProvider(
            this@EditTaskFragment,
            tasksViewModelFactory
        )[TasksViewModel::class.java]

        return binding.run {
            fragmentEditTaskDataBindingVariable = this@EditTaskFragment
            fragmentsUtilityClassVariable = fragmentsUtility
            passedTaskDate = args.taskDataArgument.taskDate?.let {
                dateFormat.format(args.taskDataArgument.taskDate!!)
            } ?: ""
            passedTaskDataDataBindingVariable = args.taskDataArgument
            lifecycleOwner = this@EditTaskFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            val passedTaskData = args.taskDataArgument

            if (passedTaskData.taskDate != null) {
                val taskIsPast = checkTaskIsPastOrNotSpecified.check(
                    passedTaskData.taskDate,
                    passedTaskData.taskTimeInString
                )

                fragmentsUtility.setTextInputEditTextTextColor(
                    requireContext(),
                    taskIsPast,
                    editTaskDateTextInputEditText,
                    editTaskTimeTextInputEditText
                )
            }

            fragmentsUtility.requestFocusAndShowKeyboard(editTaskTextInputEditText, requireActivity())

            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val editedDate = if (editTaskDateTextInputEditText.text.toString().isNotEmpty()) {
                        dateFormat.parse(editTaskDateTextInputEditText.text.toString())
                    } else null

                    val updatedTimeString =
                        editTaskTimeTextInputEditText.text.toString().ifEmpty { null }

                    val updateTimeInSeconds = fragmentsUtility.stringTimeToSeconds(
                        editTaskTimeTextInputEditText.text.toString()
                    )

                    val originalTask = args.taskDataArgument

                    val updatedTask = TaskParcel(
                        passedTaskData.position,
                        passedTaskData.taskID,
                        editTaskTextInputEditText.text.toString(),
                        editedDate,
                        updateTimeInSeconds,
                        updatedTimeString,
                    )

                    if (originalTask != updatedTask) {
                        fragmentsUtility.checkBeforeQuitingDialog(
                            requireContext(),
                            "Quit without saving changes?",
                            findNavController()
                        )
                    } else findNavController().popBackStack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

            editTaskFragmentToolbar.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.deleteTaskItem) {

                    deleteTaskDialog(
                        "Remove task?",
                        null,
                        "REMOVE",
                        "Task removed"
                    )
                }
                true
            }
        }
    }

    fun showDeleteTaskDialog() {
        deleteTaskDialog(
            "Finish task?",
            uncheckCheckBox,
            "FINISH",
            "Task finished"
        )
    }

    private val uncheckCheckBox = {
        binding.taskFinishedCheckBox.isChecked = false
    }

    fun onDateTextInputEditTextFocus(hasFocus: Boolean) {
        if (hasFocus) { showDatePickerAndGetDate(binding.editTaskDateTextInputEditText) }
    }

    fun showDatePickerAndGetDate(dateTextInputEditText: TextInputEditText) {
        binding.apply {

            fragmentsUtility.setUpDatePicker(dateTextInputEditText).also { picker ->
                picker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
                picker.addOnPositiveButtonClickListener { selectedDate ->

                    val date = Date(selectedDate)
                    val newSelectedDate = dateFormat.format(date)
                    binding.editTaskDateTextInputEditText.setText(newSelectedDate)

                    val taskDate = dateFormat.parse(editTaskDateTextInputEditText.text.toString())
                    val taskIsPast = checkTaskIsPastOrNotSpecified.check(
                        taskDate,
                        editTaskTimeTextInputEditText.text.toString()
                    )

                    fragmentsUtility.setTextInputEditTextTextColor(
                        requireContext(),
                        taskIsPast,
                        editTaskDateTextInputEditText,
                        editTaskTimeTextInputEditText
                    )
                }
            }
        }
    }

    fun onTimeTextInputEditTextFocus(hasFocus: Boolean) {
        if (hasFocus) {
            showTimePickerAndGetTime(binding.editTaskTimeTextInputEditText)
        }
    }

    fun showTimePickerAndGetTime(timeTextInputEditText: TextInputEditText) {
        binding.apply {

            val timePicker = fragmentsUtility.setUpTimePicker(timeTextInputEditText)

            timePicker.show(requireActivity().supportFragmentManager, "TIME_PICKER_TAG")
            timePicker.addOnPositiveButtonClickListener {

                tasksViewModel.hoursAndMinutesInSecondsToTime(
                    timePicker.hour.toLong(),
                    timePicker.minute.toLong()
                ).observe(viewLifecycleOwner) { newSelectedTime ->
                    binding.editTaskTimeTextInputEditText.setText(newSelectedTime)
                }

                val taskDate = dateFormat.parse(editTaskDateTextInputEditText.text.toString())
                val taskIsPast = checkTaskIsPastOrNotSpecified.check(
                    taskDate,
                    editTaskTimeTextInputEditText.text.toString()
                )

                fragmentsUtility.setTextInputEditTextTextColor(
                    requireContext(),
                    taskIsPast,
                    editTaskDateTextInputEditText,
                    editTaskTimeTextInputEditText
                )
            }
        }
    }

    fun insertEditedTaskToRoom() {
        binding.apply {

            if (editTaskTextInputEditText.text.toString().isEmpty())
                Toast.makeText(requireContext(), "Write your task", Toast.LENGTH_SHORT).show()

            else { insertTask() }
        }
    }

    private fun insertTask() {
        binding.apply {

            val passedTaskData = args.taskDataArgument

            val editedTask = editTaskTextInputEditText.text

            val taskDate = fragmentsUtility.dateInsertionLogic(editTaskDateTextInputEditText)

            val selectedTimeInString = fragmentsUtility.stringTimeInsertionLogic(
                editTaskDateTextInputEditText,
                editTaskTimeTextInputEditText
            )

            notificationsUtility.cancelSchedulingTask(
                alarmManager,
                requireContext(),
                passedTaskData.taskID
            )

            val taskTimeInSeconds = fragmentsUtility.stringTimeToSeconds(
                editTaskTimeTextInputEditText.text.toString()
            )
            val newTask = Task(
                passedTaskData.taskID,
                editedTask.toString(),
                taskDate,
                taskTimeInSeconds,
                selectedTimeInString,
            )
            tasksViewModel.updateTask(newTask)

            tasksViewModel.getTaskMillisFromTask(taskDate, selectedTimeInString)
                .observe(viewLifecycleOwner) { taskMillis ->

                    notificationsUtility.rescheduleTask(
                        requireContext(),
                        editedTask.toString(),
                        taskDate,
                        selectedTimeInString,
                        taskMillis,
                        passedTaskData.taskID
                    )
                }

            tasksViewModel.getTaskTypeFromDate(taskDate)
                .observe(viewLifecycleOwner) { taskType ->

                    val action =
                        EditTaskFragmentDirections.actionEditTaskFragmentToTasksList(
                            taskType = taskType
                        )
                    findNavController().navigate(action)
                }
        }
    }

    private fun deleteTaskDialog(
        message: String,
        uncheckCheckBox: (() -> Unit)?,
        negativeButtonText: String,
        snackBarText: String
    ) {
        MaterialAlertDialogBuilder(requireContext(), R.style.customAlertDialog)
            .setMessage(message)
            .setPositiveButton("CANCEL") { _, _ ->

                uncheckCheckBox?.invoke()
            }
            .setNegativeButton(negativeButtonText) { _, _ ->

                val taskID = args.taskDataArgument.taskID
                tasksViewModel.deleteTask(taskID)

                notificationsUtility.cancelSchedulingTask(alarmManager, requireContext(), taskID)

                notificationsUtility.cancelNotification(requireContext(), taskID)

                findNavController().popBackStack()

                fragmentToActivityContainerInterface.showTaskSnackBar(
                    binding.editTaskFragmentContainerCL,
                    snackBarText
                )
            }
            .setCancelable(false)
            .show()
    }
}