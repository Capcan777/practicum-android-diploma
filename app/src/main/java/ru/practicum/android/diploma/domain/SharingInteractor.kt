package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Vacancy

interface SharingInteractor {
    fun shareVacancy(vacancy: Vacancy)
}
