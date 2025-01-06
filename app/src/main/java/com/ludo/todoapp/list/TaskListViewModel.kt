package com.ludo.todoapp.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludo.todoapp.data.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val webService = Api.taskWebService
    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())

    fun refresh() {
        viewModelScope.launch {
            val response = webService.fetchTasks() // Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedTasks = response.body()!!
            tasksStateFlow.value = fetchedTasks // on modifie le flow, ce qui déclenche ses observers
        }
    }

    // à compléter plus tard:
    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)  // TODO: appel réseau

            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val addedTask = response.body()!!
            val updatedList = tasksStateFlow.value + addedTask
            tasksStateFlow.value = updatedList
        }
    }

    fun edit(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task)  // TODO: appel réseau

            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedTask = response.body()!!
            val updatedList = tasksStateFlow.value.map {
                if (it.id == updatedTask.id) updatedTask else it
            }
            tasksStateFlow.value = updatedList
        }
    }

    fun remove(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(task.id)  // TODO: appel réseau

            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedList = tasksStateFlow.value - task
            tasksStateFlow.value = updatedList
        }
    }
}

