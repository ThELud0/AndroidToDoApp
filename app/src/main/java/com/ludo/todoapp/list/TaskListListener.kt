package com.ludo.todoapp.list

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}