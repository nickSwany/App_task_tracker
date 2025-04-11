package com.example.tasktracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasktracker.data.db.dao.TaskDao
import com.example.tasktracker.data.db.entities.TaskEntity

@Database(
    version = 1,
    entities = [TaskEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}