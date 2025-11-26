package ru.practicum.android.diploma.ui.search.state

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data class Content(val vacancies: List<Unit>) : SearchScreenState // TODO: добавить модель
    sealed class Error() : SearchScreenState
    data object NoConnection : Error()
    data object NotFound : Error()
    data object ServerError : Error()
}
