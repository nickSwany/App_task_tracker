package com.example.tasktracker.data.impl

import com.example.tasktracker.data.db.AppDataBase
import com.example.tasktracker.domain.api.repository.CreateTaskRepository
import com.example.tasktracker.domain.convertor.TaskConvertor
import com.example.tasktracker.domain.model.Task

class CreateTaskRepositoruImpl(
    private val appDataBase: AppDataBase,
    private val convertor: TaskConvertor
) : CreateTaskRepository {

    override suspend fun createTask(task: Task) {
        appDataBase.taskDao().insert(convertor.map(task))
    }
}