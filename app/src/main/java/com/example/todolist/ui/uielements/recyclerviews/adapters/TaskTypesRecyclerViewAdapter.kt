package com.example.todolist.ui.uielements.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.domain.models.dataclasses.Task
import com.example.todolist.domain.models.enumclasses.TaskTypes
import com.example.todolist.databinding.TaskTypeDesignBinding
import com.example.todolist.ui.stateholder.viewmodels.TasksViewModel
import com.example.todolist.util.CheckTaskIsPastOrNotSpecified

class TaskTypesRecyclerViewAdapter(
    private val checkTaskIsPastOrNotSpecified: CheckTaskIsPastOrNotSpecified,
    private val overdueTasksList: Pair<String, MutableList<Task>>,
    private val todayTasksList: Pair<String, MutableList<Task>>,
    private val tomorrowTasksList: Pair<String, MutableList<Task>>,
    private val laterTasksList: Pair<String, MutableList<Task>>,
    private val noDateTasksList: Pair<String, MutableList<Task>>,
    private val tasksRecyclerViewInterface: TasksRecyclerViewInterface,
    private val context: Context,
    private val tasksViewModel: TasksViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<TaskTypesRecyclerViewAdapter.TaskTypeViewHolder>() {

    lateinit var binding: TaskTypeDesignBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTypeViewHolder {
        binding = TaskTypeDesignBinding.inflate(LayoutInflater.from(
            parent.context), parent, false)
        return TaskTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return TaskTypes.entries.size
    }

    override fun onBindViewHolder(holder: TaskTypeViewHolder, position: Int) {
        invokeSettingUpTaskTypeView(holder)
    }

    inner class TaskTypeViewHolder(
        private val binding: TaskTypeDesignBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            tasksViewModel.listIsEmptyLiveData.observe(viewLifecycleOwner) { isEmpty ->
                if (isEmpty) invokeSettingUpTaskTypeView(this)
            }
        }

        fun setUpTaskTypeView(taskTypeText: String, list: MutableList<Task>) {
            binding.apply {

                if (list.size != 0) {
                    taskTypeTextView.visibility = View.VISIBLE
                    taskTypeTextView.text = taskTypeText
                    tasksRecyclerView.visibility = View.VISIBLE
                    val adapter = TasksRecyclerViewAdapter(
                        context,
                        checkTaskIsPastOrNotSpecified,
                        list,
                        tasksRecyclerViewInterface
                    )
                    tasksRecyclerView.adapter = adapter
                    tasksRecyclerView.itemAnimator = null
                    tasksRecyclerView.layoutManager = LinearLayoutManager(context)
                } else {
                    taskTypeTextView.visibility = View.GONE
                    tasksRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    fun invokeSettingUpTaskTypeView(holder: TaskTypeViewHolder) {
        holder.apply {
            when (adapterPosition) {
                TaskTypes.OVERDUE.id -> {
                    setUpTaskTypeView(overdueTasksList.first, overdueTasksList.second)
                }

                TaskTypes.TODAY.id -> {
                    setUpTaskTypeView(todayTasksList.first, todayTasksList.second)
                }

                TaskTypes.TOMORROW.id -> {
                    setUpTaskTypeView(tomorrowTasksList.first, tomorrowTasksList.second)
                }

                TaskTypes.LATER.id -> {
                    setUpTaskTypeView(laterTasksList.first, laterTasksList.second)
                }

                TaskTypes.NO_DATE.id -> {
                    setUpTaskTypeView(noDateTasksList.first, noDateTasksList.second)
                }
            }
        }
    }
}