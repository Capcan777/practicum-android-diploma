package ru.practicum.android.diploma.data.db

import androidx.room.Database
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(entities = [VacancyEntity::class], version = 1)
abstract class AppDatabase {
    abstract fun vacancyDao(): VacancyDao
}
