package com.example.tasktracker.domain.api.interactor

import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface MainInteractor {
    suspend fun getAllTasks(): Flow<List<Task>>

    suspend fun deleteTask(task: Task)

    suspend fun approveTask(taskId: Long, isCompleted: Boolean)
}