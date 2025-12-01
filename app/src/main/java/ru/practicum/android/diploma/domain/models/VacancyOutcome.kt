package ru.practicum.android.diploma.domain.models

sealed class VacancyOutcome {
    data class Success(
        val vacancy: Vacancy
    ) : VacancyOutcome()

    data class Error(
        val type: DomainError
    ) : VacancyOutcome()
}
