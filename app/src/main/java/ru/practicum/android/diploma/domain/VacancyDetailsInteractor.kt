package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyOutcome

interface VacancyDetailsInteractor {
    suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome>
}
