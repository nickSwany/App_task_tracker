package com.example.tasktracker.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.api.repository.CreateTaskRepository
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.launch
import java.time.LocalTime

open class CreateTaskViewModel( // убрать потом open
    private val createTaskRepository: CreateTaskRepository
) : ViewModel() {

    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    private var taskStartTime by mutableStateOf<LocalTime?>(null)
    private var taskDuration by mutableStateOf<String?>(null)

    fun onTitleChange(newTitle: String) {
        taskTitle = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        taskDescription = newDescription
    }

    fun onStartTimeChange(newStartTime: LocalTime) {
        taskStartTime = newStartTime
    }

    fun onDurationChange(newDuration: String) {
        taskDuration = newDuration
    }

    fun saveTask() {
        viewModelScope.launch {
            val task = Task(
                id = System.currentTimeMillis(), // Генерируем уникальный ID
                title = taskTitle,
                description = taskDescription,
                date = null, // Пока не используем дату
                startTime = taskStartTime.toString(),
                duration = taskDuration.toString(),
                location = null,
                address = null,
                categoryName = null,
                categoryColor = null,
                status = "Запланировано",
                isCompleted = false
            )
            createTaskRepository.createTask(task)
        }
    }
}