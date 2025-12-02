package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancyToFavorites(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancies_table")
    suspend fun getFavoriteVacancies(): List<VacancyEntity>

    @Query("DELETE FROM vacancies_table WHERE vacancyId = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: String)

    @Query("SELECT COUNT(*) FROM vacancies_table WHERE vacancyId = :vacancyId")
    suspend fun isVacancyExists(vacancyId: String): Int
}
