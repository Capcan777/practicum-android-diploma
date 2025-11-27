package ru.practicum.android.diploma.ui.search.state

import ru.practicum.android.diploma.domain.models.Vacancy


data class VacancyUiModel(
    val vacancy: Vacancy,
//    val formatedSalary: String,
)

sealed interface SearchScreenState {
    data object Nothing : SearchScreenState
    data object Loading : SearchScreenState
    data class Content(val vacancies: List<VacancyUiModel>) : SearchScreenState // добавить модель
    sealed class Error : SearchScreenState
    data object NoConnection : Error()
    data object NotFound : Error()
    data object ServerError : Error()
}
