package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.SearchRequest
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
        _screenState = _screenState,
        _toastMessage = _toastMessage,
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
                searchInteractor.loadNextPage(_searchText.value, nextPage).collect { searchOutcome ->
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
                searchInteractor.loadNextPage(retryQuery, retryPage).collect { searchOutcome ->
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
        paginationState.reset()
        stateHandler.clearVacancies()
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
