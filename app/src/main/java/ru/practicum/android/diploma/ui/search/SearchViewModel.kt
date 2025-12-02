package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.ui.search.state.SearchScreenState
import ru.practicum.android.diploma.ui.search.state.VacancyUiModel
import ru.practicum.android.diploma.util.debounce
import java.io.IOException

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

    private val logTag = "SearchViewModel"

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
            try {
                searchInteractor.searchVacancies(request).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            handleSearchResult(searchOutcome, isFirstPage = true)
                        }
                        is SearchOutcome.Error -> {
                            handleError(searchOutcome)
                        }
                    }
                }
            } catch (e: HttpException) {
                Log.e(logTag, "HTTP error: ${e.message}", e)
                _screenState.value = SearchScreenState.Error.ServerError
            } catch (e: IOException) {
                Log.e(logTag, "Network error: ${e.message}", e)
                _screenState.value = SearchScreenState.Error.NoConnection
            }
        }
    }

    fun loadNextPage() {
        val nextPage = _currentPage.value + 1

        if (shouldNotLoadNextPage()) {
            return
        }

        _isLoadingNextPage.value = true

        viewModelScope.launch {
            try {
                searchInteractor.loadNextPage(_searchText.value, nextPage).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            handleSearchResult(searchOutcome, isFirstPage = false)
                            _isLoadingNextPage.value = false
                        }
                        is SearchOutcome.Error -> {
                            handleError(searchOutcome)
                            _isLoadingNextPage.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isLoadingNextPage.value = false
            }
        }
    }

    private fun shouldNotLoadNextPage(): Boolean {
        return _isLoadingNextPage.value ||
            !_hasMorePages.value ||
            _searchText.value.isEmpty()
    }

    private fun handleSearchResult(searchResult: SearchOutcome.SearchResult, isFirstPage: Boolean) {
        val vacancyUiModels: List<VacancyUiModel> = searchResult.vacancies.map {
            VacancyUiModel(it)
        }

        when {
            searchResult.vacancies.isEmpty() -> {
                if (isFirstPage) {
                    _screenState.value = SearchScreenState.Error.NotFound
                }
                _hasMorePages.value = false
            }

            else -> {
                if (isFirstPage) {
                    currentVacancies.clear()
                    currentVacancies.addAll(vacancyUiModels)
                    _currentPage.value = searchResult.currentPage
                } else {
                    currentVacancies.addAll(vacancyUiModels)
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

    fun handleError(error: SearchOutcome.Error) {
        when (error.type) {
            DomainError.NoConnection -> {
                _screenState.value = SearchScreenState.Error.NoConnection
            }
            DomainError.OtherError -> {
                _screenState.value = SearchScreenState.Error.ServerError
            }

            else -> {
                _screenState.value = SearchScreenState.Error.ServerError
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

    fun clearCountVacancies() {
        _screenState.value = SearchScreenState.Nothing
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
