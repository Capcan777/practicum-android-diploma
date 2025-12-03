package ru.practicum.android.diploma.ui.search.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaginationState {
    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage = _isLoadingNextPage.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages = _hasMorePages.asStateFlow()

    private var _retryPage: Int? = null
    private var _retryQuery: String? = null

    val retryPage: Int?
        get() = _retryPage

    val retryQuery: String?
        get() = _retryQuery

    fun setRetryInfo(page: Int, query: String) {
        _retryPage = page
        _retryQuery = query
    }

    fun clearRetryInfo() {
        _retryPage = null
        _retryQuery = null
    }

    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    fun setLoading(loading: Boolean) {
        _isLoadingNextPage.value = loading
    }

    fun setHasMorePages(hasMore: Boolean) {
        _hasMorePages.value = hasMore
    }

    fun reset() {
        _currentPage.value = 0
        _isLoadingNextPage.value = false
        _hasMorePages.value = true
        _retryPage = null
        _retryQuery = null
    }

    fun shouldNotLoadNextPage(searchText: String): Boolean {
        return _isLoadingNextPage.value ||
            !_hasMorePages.value ||
            searchText.isEmpty()
    }
}
