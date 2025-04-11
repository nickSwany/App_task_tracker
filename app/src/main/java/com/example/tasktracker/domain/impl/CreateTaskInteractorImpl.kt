package com.example.tasktracker.domain.impl

import com.example.tasktracker.domain.api.interactor.CreateTaskInteractor
import com.example.tasktracker.domain.api.repository.CreateTaskRepository
import com.example.tasktracker.domain.model.Task

class CreateTaskInteractorImpl(private val repository: CreateTaskRepository) :
    CreateTaskInteractor {
    override suspend fun createTask(task: Task) {
        return repository.createTask(task)
    }

}