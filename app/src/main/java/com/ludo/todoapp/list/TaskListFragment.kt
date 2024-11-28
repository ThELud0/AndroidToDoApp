package com.ludo.todoapp.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ludo.todoapp.R

class TaskListFragment : Fragment() {

    private var taskList = listOf(
        Task("id_1","Task 1","description 1"),
        Task("id_2","Task 2"),
        Task( "id_3","Task 3"));
    private val adapter = TaskListAdapter();


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.currentList = this.taskList;
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_recycler);
        recyclerView.adapter = adapter
    }
}