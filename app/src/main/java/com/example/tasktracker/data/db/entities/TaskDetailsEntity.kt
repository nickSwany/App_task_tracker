package com.example.tasktracker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "task_details",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["task_id"],
            childColumns = ["task-id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("task_id")]
)
data class TaskDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "detail_id")
    val detailId: Long,
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "address")
    val address: String
)
