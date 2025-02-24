package com.example.tasktracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasktracker.data.db.entities.CategoryEntity
import com.example.tasktracker.data.db.entities.TaskDetailsEntity
import com.example.tasktracker.data.db.entities.TaskEntity

@Database(
    version = 1,
    entities = [TaskEntity::class, TaskDetailsEntity::class, CategoryEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    
}