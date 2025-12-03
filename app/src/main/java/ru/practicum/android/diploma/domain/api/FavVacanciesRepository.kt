package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavVacanciesRepository {
    suspend fun addFavVacancy(vacancy: Vacancy)

    suspend fun removeFavVacancy(vacancy: Vacancy)

    suspend fun isVacancyFavorite(vacancyId: String): Boolean

    fun getFavVacancies(): Flow<List<Vacancy>>
}
