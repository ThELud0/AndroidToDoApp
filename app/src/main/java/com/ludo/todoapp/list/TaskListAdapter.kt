package com.ludo.todoapp.list

import android.app.ActivityManager.TaskDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ludo.todoapp.R


class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    var currentList: List<Task> = emptyList()

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskTitle: String,taskDescription: String) {
            val textView = itemView.findViewById<TextView>(R.id.task_title);
            textView.text = taskTitle;
            val descView = itemView.findViewById<TextView>(R.id.task_description);
            descView.text = taskDescription;
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false);
        return TaskViewHolder(itemView);
    }

    override fun getItemCount(): Int {
        return this.currentList.size;
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(this.currentList[position].title,this.currentList[position].description);
    }
}