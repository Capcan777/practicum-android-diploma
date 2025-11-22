package ru.practicum.android.diploma.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

interface VacancyDao {
    @Insert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancyToFavorites(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancies_table")
    suspend fun getFavoriteVacancies(): List<VacancyEntity>

    @Delete(entity = VacancyEntity::class)
    suspend fun deleteVacancyFromFavorites(vacancy: VacancyEntity)
}
