package com.example.tasktracker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "category_color")
    val categoryColor: String,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean
)
