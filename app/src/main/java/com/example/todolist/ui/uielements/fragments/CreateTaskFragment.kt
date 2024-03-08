package com.example.todolist.ui.uielements.fragments

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
import com.example.todolist.R
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.data.repositories.interfaces.TasksRepositoryInterface
import com.example.todolist.databinding.FragmentCreateTaskBinding
import com.example.todolist.ui.stateholder.viewmodelfactories.TasksViewModelFactory
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel
import com.example.todolist.ui.uielements.FragmentsUtility
import com.example.todolist.util.CheckTaskIsPastOrNotSpecified
import com.example.todolist.util.NotificationsUtility
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class CreateTaskFragment : Fragment(R.layout.fragment_create_task) {

    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var tasksViewModel: TasksViewModel
    @Inject lateinit var tasksRoomDatabaseRepositoryInterface: TasksRepositoryInterface
    @Inject lateinit var notificationsUtility: NotificationsUtility
    @Inject lateinit var fragmentsUtility: FragmentsUtility
    @Inject lateinit var checkTaskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecified
    private val dateFormat = ApplicationDateFormat.access()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_task,
            container,
            false
        )

        val tasksViewModelFactory =
            TasksViewModelFactory(tasksRoomDatabaseRepositoryInterface)
        tasksViewModel = ViewModelProvider(
            this@CreateTaskFragment,
            tasksViewModelFactory
        )[TasksViewModel::class.java]

        return binding.run {
            fragmentCreateTaskDataBindingVariable = this@CreateTaskFragment
            fragmentsUtilityClassVariable = fragmentsUtility
            lifecycleOwner = this@CreateTaskFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            fragmentsUtility.requestFocusAndShowKeyboard(taskTextInputEditText, requireActivity())

            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (taskTextInputEditText.text.toString().isNotEmpty() ||
                        taskDateTextInputEditText.text.toString().isNotEmpty()
                    ) {
                        fragmentsUtility.checkBeforeQuitingDialog(
                            requireContext(),
                            "Quit without saving the task?",
                            findNavController()
                        )
                    } else findNavController().popBackStack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }

    fun onDateTextInputEditTextFocus(hasFocus: Boolean) {
        if (hasFocus) {
            showDatePickerAndGetDate(binding.taskDateTextInputEditText)
        }
    }

    fun showDatePickerAndGetDate(dateTextInputEditText: TextInputEditText) {
        binding.apply {

            fragmentsUtility.setUpDatePicker(dateTextInputEditText).also { picker ->
                picker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
                picker.addOnPositiveButtonClickListener { selectedDate ->

                    val date = Date(selectedDate)
                    val stringDate = dateFormat.format(date)
                    taskDateTextInputEditText.setText(stringDate)

                    val taskDate = dateFormat.parse(taskDateTextInputEditText.text.toString())
                    val taskIsPast = checkTaskIsPastOrNotSpecified.check(
                        taskDate,
                        taskTimeTextInputEditText.text.toString()
                    )

                    fragmentsUtility.setTextInputEditTextTextColor(
                        requireContext(),
                        taskIsPast,
                        taskDateTextInputEditText,
                        taskTimeTextInputEditText
                    )
                }
            }
        }
    }

    fun onTimeTextInputEditTextFocus(hasFocus: Boolean) {
        if (hasFocus) {
            showTimePickerAndGetTime(binding.taskTimeTextInputEditText)
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
                ).observe(viewLifecycleOwner) { selectedTimeInString ->
                    taskTimeTextInputEditText.setText(selectedTimeInString)
                }

                val taskDate = dateFormat.parse(taskDateTextInputEditText.text.toString())
                val taskIsPast = checkTaskIsPastOrNotSpecified.check(
                    taskDate,
                    taskTimeTextInputEditText.text.toString()
                )

                fragmentsUtility.setTextInputEditTextTextColor(
                    requireContext(),
                    taskIsPast,
                    taskDateTextInputEditText,
                    taskTimeTextInputEditText
                )
            }
        }
    }

    fun insertTaskToRoom() {
        binding.apply {
            val task = taskTextInputEditText.text.toString().trim()

            val selectedDate = fragmentsUtility.dateInsertionLogic(taskDateTextInputEditText)

            val selectedTimeInString = fragmentsUtility.stringTimeInsertionLogic(
                taskDateTextInputEditText,
                taskTimeTextInputEditText
            )

            tasksViewModel.stringTimeToSeconds(taskTimeTextInputEditText.text.toString())
                .observe(viewLifecycleOwner) { selectedTimeInSeconds ->

                    if (taskTextInputEditText.text.toString().isEmpty()) {
                        Toast.makeText(requireContext(), "Write your task", Toast.LENGTH_SHORT)
                            .show()
                        return@observe

                    } else {
                        insertTaskAndSchedule(
                            task,
                            selectedDate,
                            selectedTimeInSeconds,
                            selectedTimeInString,
                        )

                        findNavController().popBackStack()
                    }
                }
        }
    }

    private fun insertTaskAndSchedule(
        task: String,
        selectedDate: Date?,
        selectedTimeInSeconds: Int,
        selectedTimeInString: String?,
    ) {
        val taskDetails = Task(
            0,
            task,
            selectedDate,
            selectedTimeInSeconds,
            selectedTimeInString,
        )
        tasksViewModel.insertTaskToRoom(taskDetails)

        tasksViewModel.getTaskMillisFromTask(selectedDate, selectedTimeInString)
            .observe(viewLifecycleOwner) { taskMillis ->

            tasksViewModel.getLastRowTaskID().observe(viewLifecycleOwner) { rowToBeInsertedTaskID ->

                notificationsUtility.scheduleTask(
                    requireContext(),
                    task,
                    selectedDate,
                    selectedTimeInString,
                    taskMillis,
                    rowToBeInsertedTaskID
                )
            }
        }
    }
}