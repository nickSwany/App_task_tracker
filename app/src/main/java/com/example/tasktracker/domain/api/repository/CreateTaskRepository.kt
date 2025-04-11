package com.example.tasktracker.domain.api.repository

import com.example.tasktracker.domain.model.Task

interface CreateTaskRepository {

    suspend fun createTask(task: Task)

}