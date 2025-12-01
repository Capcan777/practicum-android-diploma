package ru.practicum.android.diploma.ui.details.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface VacancyDetailsScreenState {
    data object Loading : VacancyDetailsScreenState
    data class Content(val vacancy: Vacancy) : VacancyDetailsScreenState
    sealed class Error : VacancyDetailsScreenState {
        data object NotFound : Error()
        data object ServerError : Error()
    }
}
