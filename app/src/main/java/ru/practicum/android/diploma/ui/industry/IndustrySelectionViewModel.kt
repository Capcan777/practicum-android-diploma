package ru.practicum.android.diploma.ui.industry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.IndustriesOutcome
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.industry.state.IndustrySelectionState
import java.io.IOException

class IndustrySelectionViewModel(
    private val industriesInteractor: IndustriesInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val _uiState = MutableStateFlow(IndustrySelectionState())
    val uiState = _uiState.asStateFlow()

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val outcome = industriesInteractor.getFilterIndustries().first()
                when (outcome) {
                    is IndustriesOutcome.IndustriesResult -> {
                        val saved = runCatching { filterInteractor.getFilterParameters() }.getOrNull()
                        val selected = saved?.industry

                        _uiState.update { state ->
                            val industries = outcome.industries
                            val selectedIndustry = industries.find { it.name == selected }
                            state.copy(
                                industries = industries,
                                filteredIndustries = industries,
                                isLoading = false,
                                error = null,
                                selectedIndustry = selectedIndustry
                            )
                        }
                    }

                    is IndustriesOutcome.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                error = outcome.type
                            )
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e("IndustryViewModel", "Failed to load industries due to network error", e)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = DomainError.OtherError
                    )
                }
            }
        }
    }

    fun onSearchTextChanged(query: String) {
        _uiState.update { currentState ->
            val filtered = if (query.isBlank()) {
                currentState.industries
            } else {
                currentState.industries.filter { industry ->
                    industry.name.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                filteredIndustries = filtered
            )
        }
    }

    fun onIndustrySelected(industry: Industry) {
        _uiState.update { it.copy(selectedIndustry = industry) }
    }

    fun clearSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                filteredIndustries = currentState.industries
            )
        }
    }
}
