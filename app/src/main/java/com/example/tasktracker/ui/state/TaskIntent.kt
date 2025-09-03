package com.example.tasktracker.ui.state

import com.example.tasktracker.domain.model.Task

sealed class TaskIntent {
    data object LoadTasks: TaskIntent()
    data class DeleteTask(val task: Task): TaskIntent()
    data class MarkAsDone(val task: Task): TaskIntent()
}