package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.ui.search.state.SearchScreenState
import ru.practicum.android.diploma.ui.search.state.VacancyUiModel
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _screenState = MutableStateFlow<SearchScreenState>(SearchScreenState.Nothing)
    val screenState = _screenState.asStateFlow()

    private val debounceHandler = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query ->
        searchVacancies(query)
    }

    fun clearSearchText() {
        _searchText.value = ""
        debounceHandler.cancel()
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
        debounceHandler.invoke(newText)
    }

    fun searchVacancies(query: String) {
        val request = createSearchRequest(query)
        viewModelScope.launch {
            searchInteractor.searchVacancies(request).collect { vacancyList ->
                // обработать ошибку
                if (vacancyList != null) {
                    _screenState.value = SearchScreenState.Content(
                        vacancies = vacancyList.map { vacancy ->
                            VacancyUiModel(vacancy)
                        })
                }
            }
        }
    }

    private fun createSearchRequest(query: String): SearchRequest {
        // брать параметры из фильтрации
        return SearchRequest(
            industry = null,
            text = query,
            salary = null,
            page = 1,
            onlyWithSalary = false
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
