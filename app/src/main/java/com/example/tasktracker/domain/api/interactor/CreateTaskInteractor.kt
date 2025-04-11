package com.example.tasktracker.domain.api.interactor

import com.example.tasktracker.domain.model.Task

interface CreateTaskInteractor {

    suspend fun createTask(task: Task)
}