package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

interface ExternalNavigator {
    fun shareVacancy(vacancy: Vacancy)
}
