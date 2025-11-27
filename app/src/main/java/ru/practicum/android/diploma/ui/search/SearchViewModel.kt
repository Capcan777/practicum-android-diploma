package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _vacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val vacancies = _vacancies.asStateFlow()

    fun clearSearchText() {
        _searchText.value = ""
    }

    fun searchVacancies() {
        val request = createSearchRequest()
        viewModelScope.launch {
            searchInteractor.searchVacancies(request).collect { vacancyList ->
                // обработать ошибку
                if (vacancyList != null) {
                    _vacancies.value = vacancyList
                }
            }
        }
    }

    private fun createSearchRequest(): SearchRequest {
        // брать параметры из фильтрации
        return SearchRequest(
            industry = null,
            text = _searchText.value.takeIf { it.isNotBlank() },
            salary = null,
            page = 1,
            onlyWithSalary = false
        )
    }
}
