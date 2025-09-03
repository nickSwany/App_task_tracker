package com.example.tasktracker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tasktracker.data.db.entities.TaskEntity
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE date = :date")
    suspend fun getTaskByData(date: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE task_id =:taskId ")
    suspend fun getInfoById(taskId: Long): TaskEntity

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY date, start_time")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET is_completed = :isCompleted WHERE task_id = :taskId")
    suspend fun updateIsCompleted(taskId: Long, isCompleted: Boolean)
}