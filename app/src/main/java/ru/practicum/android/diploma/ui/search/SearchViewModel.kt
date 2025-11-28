package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.SearchResult
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

    // State переменные для пагинации
    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage = _isLoadingNextPage.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages = _hasMorePages.asStateFlow()

    private var currentVacancies = mutableListOf<VacancyUiModel>()

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
        resetPagination()
        _screenState.value = SearchScreenState.Nothing
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
        if (newText.isNotEmpty()) {
            debounceHandler.invoke(newText)
        } else {
            resetPagination()
            _screenState.value = SearchScreenState.Nothing
        }
    }

    fun searchVacancies(query: String) {
        if (query.isEmpty()) return

        resetPagination()
        val request = createSearchRequest(query, 0)

        viewModelScope.launch {
            _screenState.value = SearchScreenState.Loading
            searchInteractor.searchVacancies(request).collect { searchResult ->
                handleSearchResult(searchResult, isFirstPage = true)
            }
        }
    }

    fun loadNextPage() {
        val nextPage = _currentPage.value + 1

        if (shouldNotLoadNextPage(nextPage)) {
            return
        }

        _isLoadingNextPage.value = true

        viewModelScope.launch {
            searchInteractor.loadNextPage(_searchText.value, nextPage).collect { searchResult ->
                handleSearchResult(searchResult, isFirstPage = false)
                _isLoadingNextPage.value = false
            }
        }
    }

    private fun shouldNotLoadNextPage(nextPage: Int): Boolean {
        return _isLoadingNextPage.value ||
            !_hasMorePages.value ||
            _searchText.value.isEmpty() ||
            nextPage <= _currentPage.value
    }

    private fun handleSearchResult(searchResult: SearchResult?, isFirstPage: Boolean) {
        when {
            searchResult == null -> {
                if (isFirstPage) {
                    _screenState.value = SearchScreenState.Error.ServerError
                }
                _hasMorePages.value = false
            }

            searchResult.vacancies.isEmpty() -> {
                if (isFirstPage) {
                    _screenState.value = SearchScreenState.Error.NotFound
                }
                _hasMorePages.value = false
            }

            else -> {
                val newVacancies = searchResult.vacancies.map { vacancy ->
                    VacancyUiModel(vacancy)
                }

                if (isFirstPage) {
                    currentVacancies.clear()
                    currentVacancies.addAll(newVacancies)
                    _currentPage.value = searchResult.currentPage
                } else {
                    currentVacancies.addAll(newVacancies)
                    _currentPage.value = searchResult.currentPage
                }

                _screenState.value = SearchScreenState.Content(
                    vacancies = currentVacancies.toList()
                )

                // проверка доступности страниц
                _hasMorePages.value = searchResult.currentPage < searchResult.totalPages - 1
            }
        }
    }

    private fun createSearchRequest(query: String, page: Int): SearchRequest {
        // брать параметры из фильтрации
        return SearchRequest(
            industry = null,
            text = query,
            salary = null,
            page = page,
            onlyWithSalary = false
        )
    }

    private fun resetPagination() {
        _currentPage.value = 0
        _isLoadingNextPage.value = false
        _hasMorePages.value = true
        currentVacancies.clear()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
