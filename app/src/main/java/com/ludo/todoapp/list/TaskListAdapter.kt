package com.ludo.todoapp.list

import android.app.ActivityManager.TaskDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ludo.todoapp.R
import com.ludo.todoapp.databinding.FragmentTaskListBinding
import com.ludo.todoapp.databinding.ItemTaskBinding


object MyItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id// comparaison: est-ce la même "entité" ? => même id?
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem == newItem// comparaison: est-ce le même "contenu" ? => mêmes valeurs? (avec data class: simple égalité)
    }
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyItemsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description

            binding.editButton.setOnClickListener {
                listener.onClickEdit(task) // Trigger edit action
            }

            binding.deleteButton.setOnClickListener {
                listener.onClickDelete(task) // Trigger delete action
            }
        }
    }
}

