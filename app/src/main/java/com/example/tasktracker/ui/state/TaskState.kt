package com.example.tasktracker.ui.state

import com.example.tasktracker.domain.model.Task

sealed class TaskState {
    data object Loading : TaskState()
    data class Success(val tasks: List<Task>) : TaskState()
    data class Error(val message: String) : TaskState()
}