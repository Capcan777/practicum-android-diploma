package ru.practicum.android.diploma.ui.search

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
import ru.practicum.android.diploma.ui.common.Event
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

    private val _toastMessage = MutableStateFlow<Event<String>?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    private var _retryPage: Int? = null
    private var _retryQuery: String? = null

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
            try {
                searchInteractor.searchVacancies(request).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            handleSearchResult(searchOutcome, isFirstPage = true)
                        }

                        is SearchOutcome.Error -> {
                            handleError(searchOutcome, isFirstPage = true)
                        }
                    }
                }
            } catch (_: HttpException) {
                _screenState.value = SearchScreenState.Error.ServerError
            } catch (_: IOException) {
                _screenState.value = SearchScreenState.Error.NoConnection
            }
        }
    }

    fun loadNextPage() {
        val nextPage = _currentPage.value + 1

        if (shouldNotLoadNextPage()) {
            return
        }

        _retryPage = nextPage
        _retryQuery = _searchText.value

        _isLoadingNextPage.value = true

        viewModelScope.launch {
            try {
                searchInteractor.loadNextPage(_searchText.value, nextPage).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            handleSearchResult(searchOutcome, isFirstPage = false)
                            _retryPage = null
                            _retryQuery = null
                        }

                        is SearchOutcome.Error -> {
                            handleError(searchOutcome, isFirstPage = false)
                        }
                    }
                }
            } catch (_: IOException) {
                handlePaginationError("Нет подключения к интернету")
            } catch (_: HttpException) {
                handlePaginationError("Ошибка сервера")
            } finally {
                _isLoadingNextPage.value = false
            }
        }
    }

    fun retryLastFailedPage() {
        val retryPage = _retryPage ?: return
        val retryQuery = _retryQuery ?: return

        _isLoadingNextPage.value = true

        viewModelScope.launch {
            try {
                searchInteractor.loadNextPage(retryQuery, retryPage).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            handleSearchResult(searchOutcome, isFirstPage = false)
                            _retryPage = null
                            _retryQuery = null
                        }

                        is SearchOutcome.Error -> {
                            handleError(searchOutcome, isFirstPage = false)
                        }
                    }
                }
            } catch (_: IOException) {
                handlePaginationError("Не удалось загрузить данные. Проверьте подключение")
            } catch (_: HttpException) {
                handlePaginationError("Ошибка сервера при загрузке данных")
            } finally {
                _isLoadingNextPage.value = false
            }
        }
    }

    private fun handlePaginationError(message: String) {
        _toastMessage.value = Event(message)
        _isLoadingNextPage.value = false
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

                _hasMorePages.value = searchResult.currentPage < searchResult.totalPages - 1
            }
        }
    }

    private fun handleError(error: SearchOutcome.Error, isFirstPage: Boolean) {
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
            // Пока оставил тут, так как нет конкретных ошибок, если что перенесём
            val errorMessage = when (error.type) {
                DomainError.NoConnection -> "Не удалось загрузить данные. Проверьте подключение"
                DomainError.NotFound -> "Данные не найдены"
                DomainError.OtherError -> "Ошибка сервера при загрузке данных"
            }
            _toastMessage.value = Event(errorMessage)
            _isLoadingNextPage.value = false
        }
    }

    private fun createSearchRequest(query: String, page: Int): SearchRequest {
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
        _retryPage = null
        _retryQuery = null
        currentVacancies.clear()
    }

    fun clearCountVacancies() {
        _screenState.value = SearchScreenState.Nothing
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
