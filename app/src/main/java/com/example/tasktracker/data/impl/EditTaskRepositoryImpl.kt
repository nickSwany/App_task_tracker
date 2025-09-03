package com.example.tasktracker.data.impl

import com.example.tasktracker.data.db.AppDataBase
import com.example.tasktracker.domain.api.repository.EditTaskRepository
import com.example.tasktracker.domain.convertor.TaskConvertor
import com.example.tasktracker.domain.model.Task

class EditTaskRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val convertor: TaskConvertor
) : EditTaskRepository {
    override suspend fun getInfoTaskById(taskId: Long): Task {
        return convertor.mapToTask(appDataBase.taskDao().getInfoById(taskId))
    }
}