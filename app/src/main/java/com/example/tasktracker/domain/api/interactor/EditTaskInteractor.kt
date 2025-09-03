package com.example.tasktracker.domain.api.interactor

import com.example.tasktracker.domain.model.Task

interface EditTaskInteractor {
    suspend fun getInfoTaskById(taskId: Long): Task
}