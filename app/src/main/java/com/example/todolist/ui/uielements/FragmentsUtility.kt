package com.example.todolist.ui.uielements

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.todolist.R
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.application.objects.ApplicationTimeFormat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class FragmentsUtility {

    private val timeFormat = ApplicationTimeFormat.access()
    private val dateFormat = ApplicationDateFormat.access()

    fun requestFocusAndShowKeyboard(
        textInputEditText: TextInputEditText,
        fragmentActivity: FragmentActivity
    ) {
        textInputEditText.requestFocus()
        val inputMethodManager =
            fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun setUpDatePicker(dateTextInputEditText: TextInputEditText): MaterialDatePicker<Long> {
        val dateToBeShown = if (dateTextInputEditText.text.toString().isEmpty()) {
            MaterialDatePicker.todayInUtcMilliseconds()
        } else {
            dateFormat.parse(dateTextInputEditText.text.toString())!!.time
        }
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.customDatePicker)
        builder.setSelection(dateToBeShown)

        return builder.build()
    }

    @SuppressLint("SimpleDateFormat")
    fun setUpTimePicker(timeTextInputEditText: TextInputEditText): MaterialTimePicker {

        val hoursAndMinutesPair = if (timeTextInputEditText.text.isNullOrEmpty()) {
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)
            Pair(currentHour, currentMinute)

        } else {
            val selectedTimeInMillis =
                timeFormat.parse(timeTextInputEditText.text.toString())!!.time

            val twentyFourHourTimeSDF = SimpleDateFormat("HH:mm")
            val selectedTimeInTwentyFourHours =
                twentyFourHourTimeSDF.format(selectedTimeInMillis)

            val selectedHours = selectedTimeInTwentyFourHours.take(2).toInt()
            val selectedMinute = selectedTimeInTwentyFourHours.drop(3).take(2).toInt()

            Pair(selectedHours, selectedMinute)
        }

        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hoursAndMinutesPair.first)
            .setMinute(hoursAndMinutesPair.second)
            .setTheme(R.style.customTimePicker)
            .build()
    }

    fun checkBeforeQuitingDialog(
        context: Context,
        message: String,
        navController: NavController,
    ) {
        MaterialAlertDialogBuilder(context, R.style.customAlertDialog)
            .setMessage(message)
            .setPositiveButton("DISMISS") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("QUIT") { dialog, _ ->

                dialog.dismiss()
                navController.popBackStack()
            }
            .show()
    }

    fun dateTextInputEditTextTextWatcher(
        s: CharSequence,
        removeDateImageView: ImageView,
        timeConstraintLayout: ConstraintLayout,
        timeTextInputEditText: TextInputEditText
    ) {
        if (s.toString().isEmpty()) {
            removeDateImageView.visibility = View.GONE
            timeConstraintLayout.visibility = View.GONE
            timeTextInputEditText.text!!.clear()

        } else {
            removeDateImageView.visibility = View.VISIBLE
            timeConstraintLayout.visibility = View.VISIBLE
        }
    }

    fun timeTextInputEditTextTextWatcher(
        s: CharSequence,
        removeTimeInputImageView: ImageView
    ) {
        if (s.toString().isNotEmpty()) {
            removeTimeInputImageView.visibility = View.VISIBLE
        } else {
            removeTimeInputImageView.visibility = View.GONE
        }
    }

    fun dateInsertionLogic(dateTextInputEditText: TextInputEditText): Date? {
        val dateFormat = ApplicationDateFormat.access()
        return if (dateTextInputEditText.text.toString() != "") {
            dateFormat.parse(dateTextInputEditText.text.toString())
        } else null
    }

    fun stringTimeInsertionLogic(
        dateTextInputEditText: TextInputEditText,
        timeTextInputEditText: TextInputEditText
    ): String? {

        val date = dateTextInputEditText.text.toString()
        val time = timeTextInputEditText.text.toString()

        return when {
            date.isNotEmpty() && time.isNotEmpty() -> timeTextInputEditText.text.toString()
            date.isNotEmpty() && time.isEmpty() -> "00:00 AM"
            date.isEmpty() -> null
            else -> null
        }
    }

    fun clearInput(textInputEditText: TextInputEditText) {
        textInputEditText.text!!.clear()
    }

    fun setTextInputEditTextTextColor(
        context: Context,
        isPast: Boolean,
        dateTextInputEditText: TextInputEditText,
        timeTextInputEditText: TextInputEditText,
    ) {

        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val redColor = ContextCompat.getColor(context, R.color.red)

        if (isPast) {
            dateTextInputEditText.setTextColor(redColor)
            timeTextInputEditText.setTextColor(redColor)
        } else {
            dateTextInputEditText.setTextColor(whiteColor)
            timeTextInputEditText.setTextColor(whiteColor)
        }
    }
}