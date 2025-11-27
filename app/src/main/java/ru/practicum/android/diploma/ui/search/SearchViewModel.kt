package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.ui.search.state.SearchScreenState

class SearchViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val screenState = MutableStateFlow<SearchScreenState>(SearchScreenState.Nothing)
    val screenStateFlow = screenState.asStateFlow()

    fun clearSearchText() {
        _searchText.value = ""
    }
}
