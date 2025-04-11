package com.example.tasktracker.di

import com.example.tasktracker.ui.viewModel.CreateTaskViewModel
import com.example.tasktracker.ui.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        CreateTaskViewModel(get())
    }
}