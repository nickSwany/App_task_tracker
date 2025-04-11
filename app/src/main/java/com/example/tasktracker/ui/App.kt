package com.example.tasktracker.ui

import android.app.Application
import com.example.tasktracker.di.dataModule
import com.example.tasktracker.di.interactorModule
import com.example.tasktracker.di.repositoryModule
import com.example.tasktracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    interactorModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}