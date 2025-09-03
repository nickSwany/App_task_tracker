package com.example.tasktracker.domain.impl

import com.example.tasktracker.domain.api.interactor.EditTaskInteractor
import com.example.tasktracker.domain.api.repository.EditTaskRepository
import com.example.tasktracker.domain.model.Task

class EditTaskInteractorImpl(private val repository: EditTaskRepository) : EditTaskInteractor {

    override suspend fun getInfoTaskById(taskId: Long): Task {
        return repository.getInfoTaskById(taskId)
    }
}