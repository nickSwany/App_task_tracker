package com.example.tasktracker.domain.convertor

import com.example.tasktracker.data.db.entities.TaskEntity
import com.example.tasktracker.domain.model.Task

class TaskConvertor() {

    fun mapToTask(taskEntity: TaskEntity): Task {
        return Task(
            id = taskEntity.id,
            title = taskEntity.title,
            description = taskEntity.description,
            date = taskEntity.date,
            startTime = taskEntity.startTime,
            duration = taskEntity.duration,
            location = taskEntity.location,
            address = taskEntity.address,
            categoryName = taskEntity.categoryName,
            categoryColor = taskEntity.categoryColor,
            status = taskEntity.status,
            isCompleted = taskEntity.isCompleted
        )
    }

    fun map(task: Task): TaskEntity {
        return TaskEntity(
            id = task.id,
            title = task.title.toString(),
            description = task.description.toString(),
            date = task.date.toString(),
            startTime = task.startTime.toString(),
            duration = task.duration.toString(),
            location = task.location.toString(),
            address = task.address.toString(),
            categoryName = task.categoryName.toString(),
            categoryColor = task.categoryColor.toString(),
            status = task.status.toString(),
            isCompleted = task.isCompleted 
        )
    }

    fun convertToListTask(tasks: List<TaskEntity>): List<Task> {
        return tasks.map { task ->
            mapToTask(task)
        }
    }

}
