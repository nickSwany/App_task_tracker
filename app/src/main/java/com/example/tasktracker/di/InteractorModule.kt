package com.example.tasktracker.di

import com.example.tasktracker.data.impl.MainRepositoryImpl
import com.example.tasktracker.domain.api.interactor.CreateTaskInteractor
import com.example.tasktracker.domain.api.interactor.MainInteractor
import com.example.tasktracker.domain.impl.CreateTaskInteractorImpl
import com.example.tasktracker.domain.impl.MainInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<MainInteractor> {
        MainInteractorImpl(get())
    }


    single<CreateTaskInteractor> {
        CreateTaskInteractorImpl(get())
    }
}