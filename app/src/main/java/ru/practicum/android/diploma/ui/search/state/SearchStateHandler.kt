package ru.practicum.android.diploma.ui.search.state

import kotlinx.coroutines.flow.MutableStateFlow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.ui.common.Event
import ru.practicum.android.diploma.util.ResourceProvider

class SearchStateHandler(
    private val _screenState: MutableStateFlow<SearchScreenState>,
    private val _toastMessage: MutableStateFlow<Event<String>?>,
    private val paginationState: PaginationState,
    private val resourceProvider: ResourceProvider
) {
    private val currentVacancies = mutableListOf<VacancyUiModel>()

    fun handleSearchResult(searchResult: SearchOutcome.SearchResult, isFirstPage: Boolean) {
        val vacancyUiModels: List<VacancyUiModel> = searchResult.vacancies.map {
            VacancyUiModel(it)
        }

        when {
            searchResult.vacancies.isEmpty() -> {
                if (isFirstPage) {
                    _screenState.value = SearchScreenState.Error.NotFound
                }
                paginationState.setHasMorePages(false)
            }

            else -> {
                if (isFirstPage) {
                    currentVacancies.clear()
                    currentVacancies.addAll(vacancyUiModels)
                    paginationState.updatePage(searchResult.currentPage)
                } else {
                    currentVacancies.addAll(vacancyUiModels)
                    paginationState.updatePage(searchResult.currentPage)
                }

                _screenState.value = SearchScreenState.Content(
                    vacancies = currentVacancies.toList(),
                    foundCount = searchResult.found
                )

                paginationState.setHasMorePages(
                    searchResult.currentPage < searchResult.totalPages - 1
                )
            }
        }
    }

    fun handleError(error: SearchOutcome.Error, isFirstPage: Boolean) {
        if (isFirstPage) {
            when (error.type) {
                DomainError.NoConnection -> {
                    _screenState.value = SearchScreenState.Error.NoConnection
                }

                DomainError.NotFound -> {
                    _screenState.value = SearchScreenState.Error.NotFound
                }

                DomainError.OtherError -> {
                    _screenState.value = SearchScreenState.Error.ServerError
                }
            }
        } else {
            val errorMessage = when (error.type) {
                DomainError.NoConnection -> resourceProvider.getString(R.string.error_failed_to_load_data)
                DomainError.NotFound -> resourceProvider.getString(R.string.error_data_not_found)
                DomainError.OtherError -> resourceProvider.getString(R.string.error_server_loading_data)
            }
            _toastMessage.value = Event(errorMessage)
            paginationState.setLoading(false)
        }
    }

    fun handlePaginationError(message: String) {
        _toastMessage.value = Event(message)
        paginationState.setLoading(false)
    }

    fun clearVacancies() {
        currentVacancies.clear()
    }
}
