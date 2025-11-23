package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.AppDatabase

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            DatabaseConfig.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().vacancyDao() }
}

class DatabaseConfig {
    companion object {
        const val DATABASE_NAME = "vacancy_db.db"
    }
}
