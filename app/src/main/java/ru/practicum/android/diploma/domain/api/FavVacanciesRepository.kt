package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

interface FavVacanciesRepository {
    suspend fun addFavVacancy(vacancy: Vacancy)

    suspend fun removeFavVacancy(vacancy: Vacancy)

    suspend fun isVacancyFavorite(vacancyId: String): Boolean
}
