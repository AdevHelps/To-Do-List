package com.example.todolist.ui.uielements.recyclerviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.application.objects.ApplicationDateFormat
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.databinding.TaskItemDesignBinding
import com.example.todolist.ui.uielements.recyclerviews.TasksRecyclerViewInterface
import com.example.todolist.util.CheckTaskIsPastOrNotSpecifiedUtility
import java.util.Date

class TasksRecyclerViewAdapter(
    private val context: Context,
    private val checkTaskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecifiedUtility,
    private val tasksList: MutableList<Task>,
    private val tasksRecyclerViewInterface: TasksRecyclerViewInterface
) : RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>() {

    lateinit var binding: TaskItemDesignBinding
    private val dateFormat = ApplicationDateFormat.access()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TaskViewHolder {
        binding = TaskItemDesignBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]

        holder.bind(task)

        setTextViewTextColor(
            task.taskDate,
            task.taskTimeInString,
            holder.binding.RVItemTaskDateTV
        )
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(
        val binding: TaskItemDesignBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.also { binding ->
                binding.task = task
                binding.taskDateAndTime = if (task.taskDate != null) {
                    val dateFormatted= dateFormat.format(task.taskDate)

                    when {
                        task.taskTimeInString.isNullOrEmpty() -> { dateFormatted }
                        task.taskTimeInString.isNotEmpty() -> {
                            "$dateFormatted at ${task.taskTimeInString}"
                        }
                        else -> { "" }
                    }
                } else ""
                binding.executePendingBindings()
            }
        }

        init {
            binding.apply {
                RVItemCardView.setOnClickListener {
                    val task = tasksList[adapterPosition]
                    tasksRecyclerViewInterface.onClickAction(adapterPosition, task)
                }

                RVItemTaskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        val task = tasksList[adapterPosition]
                        tasksRecyclerViewInterface.onCheckBoxChecked(
                            tasksList.size,
                            task.taskID,
                            binding.RVItemTaskCheckBox,
                            ::notifyRemoved
                        )
                    }
                }
            }
        }

        private fun notifyRemoved() {
            tasksList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    private fun setTextViewTextColor(
        taskDate: Date?,
        stringTaskTime: String?,
        dateTextView: TextView
    ) {
        val taskIsPast = checkTaskIsPastOrNotSpecified.check(taskDate, stringTaskTime)

        val paleWhiteColor = ContextCompat.getColor(context, R.color.paleWhite)
        val redColor = ContextCompat.getColor(context, R.color.red)

        if (taskIsPast) {
            dateTextView.setTextColor(redColor)
        } else {
            dateTextView.setTextColor(paleWhiteColor)
        }
    }
}