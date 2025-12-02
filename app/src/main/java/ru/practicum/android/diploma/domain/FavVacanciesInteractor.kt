package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Vacancy

interface FavVacanciesInteractor {
    suspend fun addFavVacancy(vacancy: Vacancy)

    suspend fun removeFavVacancy(vacancy: Vacancy)

    suspend fun isVacancyFavorite(vacancyId: String): Boolean
}
