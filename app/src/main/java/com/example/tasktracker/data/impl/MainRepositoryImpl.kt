package com.example.tasktracker.data.impl

import com.example.tasktracker.data.db.AppDataBase
import com.example.tasktracker.domain.api.repository.MainRepository
import com.example.tasktracker.domain.convertor.TaskConvertor
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MainRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val convertor: TaskConvertor
) : MainRepository {

    override suspend fun getAllTasks(): Flow<List<Task>> {
        return appDataBase.taskDao().getAllTasks().map { tasks ->
            convertor.convertToListTask(tasks)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTask(task: Task) {
        return appDataBase.taskDao().delete(convertor.map(task))
    }
}