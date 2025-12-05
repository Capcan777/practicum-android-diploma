package ru.practicum.android.diploma.ui.search.state

import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyUiModel(
    val vacancy: Vacancy,
    val salaryDisplay: String? = null
)

sealed interface SearchScreenState {
    data object Nothing : SearchScreenState
    data object Loading : SearchScreenState
    data class Content(
        val vacancies: List<VacancyUiModel>,
        val foundCount: Int
    ) : SearchScreenState
    sealed class Error : SearchScreenState {
        data object NoConnection : Error()
        data object NotFound : Error()
        data object ServerError : Error()
    }
}
