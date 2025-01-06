package com.ludo.todoapp.list

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ludo.todoapp.R
import com.ludo.todoapp.data.Api
import com.ludo.todoapp.databinding.FragmentTaskListBinding

import com.ludo.todoapp.detail.DetailActivity
import kotlinx.coroutines.launch

import java.util.UUID


class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()

    val adapterListener : TaskListListener = object : TaskListListener {

        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task) // Send the task to be edited
            editTask.launch(intent)
        }
    }

    val adapter = TaskListAdapter(adapterListener)


    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val updatedTask = result.data?.getSerializableExtra("task") as? Task
        updatedTask?.let{
            viewModel.add(updatedTask)
        }
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val updatedTask = result.data?.getSerializableExtra("task") as? Task
        updatedTask?.let{
            viewModel.edit(updatedTask)
1        }
    }

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentRecycler.adapter = adapter

        // FAB for adding a new task
        binding.fab.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            val newTask = Task(UUID.randomUUID().toString(), "Task ")
            intent.putExtra("task", newTask) // Send the new task to DetailActivity
            createTask.launch(intent)
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }

    }

    override fun onResume(){
        super.onResume()
        viewModel.refresh()
        // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding.userTextView.text = user.name
            binding.userImageView.load("https://goo.gl/gEgYUd")
        }

    }


}

