package com.example.tasktracker.domain.model

data class Task(
    val id: Long,
    val title: String?,
    val description: String?,
    val date: String?,
    val startTime: String?,
    val duration: String?,
    val location: String?,
    val address: String?,
    val categoryName: String?,
    val categoryColor: String?,
    val status: String?,
    val isCompleted: Boolean
)
