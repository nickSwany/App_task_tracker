package com.example.tasktracker.domain.impl

import com.example.tasktracker.domain.api.interactor.MainInteractor
import com.example.tasktracker.domain.api.repository.MainRepository
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {
    override suspend fun getAllTasks(): Flow<List<Task>> {
        return repository.getAllTasks()
    }

    override suspend fun deleteTask(task: Task) {
        return repository.deleteTask(task)
    }

    override suspend fun approveTask(taskId: Long, isCompleted: Boolean) {
        return repository.approveTask(taskId, isCompleted)
    }
}