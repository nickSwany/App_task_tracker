package com.example.tasktracker.di

import androidx.room.Room
import com.example.tasktracker.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<AppDataBase> {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}