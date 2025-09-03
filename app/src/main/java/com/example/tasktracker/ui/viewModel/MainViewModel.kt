package com.example.tasktracker.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.api.interactor.MainInteractor
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.ui.state.TaskIntent
import com.example.tasktracker.ui.state.TaskState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {

    private val _state = MutableStateFlow<TaskState>(TaskState.Loading)
    val tasks: StateFlow<TaskState> = _state.asStateFlow()

    private val intents = Channel<TaskIntent>(Channel.UNLIMITED)

    init {
        handleIntents()
        intents.trySend(TaskIntent.LoadTasks)
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { intent ->
                when (intent) {
                    is TaskIntent.LoadTasks -> loadTasks()
                    is TaskIntent.DeleteTask -> transferTask(intent.task)
                    is TaskIntent.MarkAsDone -> transferTask(intent.task)
                }
            }
        }
    }

    private suspend fun loadTasks() {
        _state.value = TaskState.Loading
        try {
            interactor.getAllTasks().collect { taskList ->
                _state.value = TaskState.Success(taskList)
            }
        } catch (e: Exception) {
            _state.value = TaskState.Error(e.message ?: "Error")
        }
    }

    private suspend fun transferTask(task: Task) {
        try {
            interactor.deleteTask(task)
            intents.send(TaskIntent.LoadTasks)
        } catch (e: Exception) {
            _state.value = TaskState.Error("Ошибка уданеия задачи")
        }
    }

    fun approve(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    interactor.approveTask(taskId, isCompleted)
                    loadTasks()
                } catch (e: Exception) {
                    _state.value = TaskState.Error("Ошибка обновления задачи")
                }
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    interactor.deleteTask(task)
                    loadTasks()
                } catch (e: Exception) {
                    _state.value = TaskState.Error("Ошибка удаления задачи")
                }
            }
        }
    }
}