package com.example.tasktracker.di

import com.example.tasktracker.data.impl.CreateTaskRepositoruImpl
import com.example.tasktracker.data.impl.MainRepositoryImpl
import com.example.tasktracker.domain.api.repository.CreateTaskRepository
import com.example.tasktracker.domain.api.repository.MainRepository
import com.example.tasktracker.domain.convertor.TaskConvertor
import org.koin.dsl.module

val repositoryModule = module {

    factory { TaskConvertor() }

    single<MainRepository> {
        MainRepositoryImpl(get(), get())
    }

    single<CreateTaskRepository> {
       CreateTaskRepositoruImpl(get(), get())
    }

}