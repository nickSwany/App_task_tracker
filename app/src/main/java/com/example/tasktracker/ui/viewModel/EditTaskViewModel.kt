package com.example.tasktracker.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.api.interactor.EditTaskInteractor
import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val interactor: EditTaskInteractor
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    fun loadTask(taskId: Long) = viewModelScope.launch {
        try {
            val task = interactor.getInfoTaskById(taskId)
            _task.value = task
        } catch (e: Exception) {
            Log.e("EditTaskViewModel", "Error loading task: ${e.message}")
            throw e
        }
    }
}