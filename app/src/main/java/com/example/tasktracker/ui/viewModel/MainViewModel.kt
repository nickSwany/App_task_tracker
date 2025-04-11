package com.example.tasktracker.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.api.interactor.MainInteractor
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            interactor.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            interactor.deleteTask(task)
        }
    }
}