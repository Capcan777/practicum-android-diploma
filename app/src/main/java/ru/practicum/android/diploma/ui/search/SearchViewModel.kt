package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.FilterParameters
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.ui.common.Event
import ru.practicum.android.diploma.ui.search.state.PaginationState
import ru.practicum.android.diploma.ui.search.state.SearchScreenState
import ru.practicum.android.diploma.ui.search.state.SearchStateHandler
import ru.practicum.android.diploma.util.ResourceProvider
import ru.practicum.android.diploma.util.debounce
import java.io.IOException

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _screenState = MutableStateFlow<SearchScreenState>(SearchScreenState.Nothing)
    val screenState = _screenState.asStateFlow()

    private val _toastMessage = MutableStateFlow<Event<String>?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    private val paginationState = PaginationState()
    val currentPage = paginationState.currentPage
    val isLoadingNextPage = paginationState.isLoadingNextPage
    val hasMorePages = paginationState.hasMorePages

    private val stateHandler = SearchStateHandler(
        screenState = _screenState,
        toastMessage = _toastMessage,
        paginationState = paginationState,
        resourceProvider = resourceProvider
    )

    private val debounceHandler = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query ->
        searchVacancies(query)
    }

    private val _filterParameters = MutableStateFlow(FilterParameters())
    val filterParameters: StateFlow<FilterParameters> = _filterParameters.asStateFlow()

    private val _areFiltersApplied = MutableStateFlow(false)
    val areFiltersApplied: StateFlow<Boolean> = _areFiltersApplied.asStateFlow()

    init {
        viewModelScope.launch {
            val savedFilters = filterInteractor.getFilterParameters()
            _filterParameters.value = savedFilters
            checkIfFiltersApplied(savedFilters)

            filterInteractor.filterUpdates.collect { params ->
                _filterParameters.value = params
                val query = _searchText.value
                if (query.isNotEmpty()) {
                    searchVacancies(query)
                } else {
                    resetPagination()
                    _screenState.value = SearchScreenState.Nothing
                }
            }
        }
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
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            val request = createSearchRequest(query, 0)
            try {
                searchInteractor.searchVacancies(request).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            stateHandler.handleSearchResult(searchOutcome, isFirstPage = true)
                        }

                        is SearchOutcome.Error -> {
                            stateHandler.handleError(searchOutcome, isFirstPage = true)
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
        val nextPage = currentPage.value + 1

        if (paginationState.shouldNotLoadNextPage(_searchText.value)) {
            return
        }

        paginationState.setRetryInfo(nextPage, _searchText.value)
        paginationState.setLoading(true)

        viewModelScope.launch {
            try {
                val request = createSearchRequest(_searchText.value, nextPage)
                searchInteractor.searchVacancies(request).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            stateHandler.handleSearchResult(searchOutcome, isFirstPage = false)
                            paginationState.clearRetryInfo()
                        }

                        is SearchOutcome.Error -> {
                            stateHandler.handleError(searchOutcome, isFirstPage = false)
                        }
                    }
                }
            } catch (_: IOException) {
                stateHandler.handlePaginationError(
                    resourceProvider.getString(R.string.error_no_internet_connection)
                )
            } catch (_: HttpException) {
                stateHandler.handlePaginationError(
                    resourceProvider.getString(R.string.server_error)
                )
            } finally {
                paginationState.setLoading(false)
            }
        }
    }

    fun retryLastFailedPage() {
        val retryPage = paginationState.retryPage ?: return
        val retryQuery = paginationState.retryQuery ?: return

        paginationState.setLoading(true)

        viewModelScope.launch {
            try {
                val request = createSearchRequest(retryQuery, retryPage)
                searchInteractor.searchVacancies(request).collect { searchOutcome ->
                    when (searchOutcome) {
                        is SearchOutcome.SearchResult -> {
                            stateHandler.handleSearchResult(searchOutcome, isFirstPage = false)
                            paginationState.clearRetryInfo()
                        }

                        is SearchOutcome.Error -> {
                            stateHandler.handleError(searchOutcome, isFirstPage = false)
                        }
                    }
                }
            } catch (_: IOException) {
                stateHandler.handlePaginationError(
                    resourceProvider.getString(R.string.error_failed_to_load_data)
                )
            } catch (_: HttpException) {
                stateHandler.handlePaginationError(
                    resourceProvider.getString(R.string.error_server_loading_data)
                )
            } finally {
                paginationState.setLoading(false)
            }
        }
    }

    private suspend fun createSearchRequest(query: String, page: Int): SearchRequest {
        val filterParameters = filterInteractor.getFilterParameters()
        return SearchRequest(
            industry = filterParameters.industry.takeIf { it.isNotEmpty() }?.toIntOrNull(),
            text = query,
            salary = filterParameters.salary.takeIf { it.isNotEmpty() }?.toIntOrNull(),
            page = page,
            onlyWithSalary = filterParameters.hideWithoutSalary
        )
    }

    private fun resetPagination() {
        paginationState.reset()
        stateHandler.clearVacancies()
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun refreshSearchWithCurrentQuery() {
        val currentQuery = _searchText.value
        if (currentQuery.isNotEmpty()) {
            // Отменяем текущий debounce и сразу запускаем поиск
            debounceHandler.cancel()
            searchVacancies(currentQuery)
        }
    }

    private fun checkIfFiltersApplied(params: FilterParameters) {
        _areFiltersApplied.value = params.salary.isNotEmpty() ||
            params.industry.isNotEmpty() ||
            params.placeOfWork.isNotEmpty() ||
            params.hideWithoutSalary
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
