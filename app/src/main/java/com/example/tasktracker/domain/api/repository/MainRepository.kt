package com.example.tasktracker.domain.api.repository

import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface MainRepository {
     suspend fun getAllTasks() : Flow<List<Task>>

     suspend fun deleteTask(task: Task)

     suspend fun approveTask(task: Long, isCompleted: Boolean)
}