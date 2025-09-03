package com.example.tasktracker.domain.api.repository

import com.example.tasktracker.domain.model.Task

interface EditTaskRepository {
    suspend fun getInfoTaskById(taskId: Long): Task
}