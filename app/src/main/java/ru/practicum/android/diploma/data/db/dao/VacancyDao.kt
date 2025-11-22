package ru.practicum.android.diploma.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

interface VacancyDao {
    @Insert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancyToFavorites(vacancy: VacancyEntity)

    suspend fun getFavoriteVacancies(): List<VacancyEntity>

    suspend fun deleteVacancyFromFavorites(vacancy: VacancyEntity)
}
