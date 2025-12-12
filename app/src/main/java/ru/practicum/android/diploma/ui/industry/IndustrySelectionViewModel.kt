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
                handleIndustriesOutcome(outcome)
            } catch (e: IOException) {
                Log.e("IndustryViewModel", "Failed to load industries", e)
                updateStateWithError(DomainError.OtherError)
            }
        }
    }

    private suspend fun handleIndustriesOutcome(outcome: IndustriesOutcome) {
        when (outcome) {
            is IndustriesOutcome.IndustriesResult -> handleIndustriesResult(outcome)
            is IndustriesOutcome.Error -> updateStateWithError(outcome.type)
        }
    }

    private suspend fun handleIndustriesResult(result: IndustriesOutcome.IndustriesResult) {
        val savedIndustry = runCatching { filterInteractor.getFilterParameters() }
            .getOrNull()
            ?.industry

        val selectedIndustry = result.industries.find { it.name == savedIndustry }

        _uiState.update { state ->
            state.copy(
                industries = result.industries,
                filteredIndustries = result.industries,
                isLoading = false,
                error = null,
                selectedIndustry = selectedIndustry
            )
        }
    }

    private fun updateStateWithError(errorType: DomainError) {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                error = errorType
            )
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
        _uiState.update { currentState ->
            currentState.copy(
                selectedIndustry = industry,
                isUserSelected = true,
                searchQuery = industry.name,
                filteredIndustries = currentState.industries.filter { it.name.contains(industry.name, ignoreCase = true) }
            )
        }
    }

    fun clearSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                filteredIndustries = currentState.industries,
                selectedIndustry = null,
                isUserSelected = false
            )
        }
    }

    fun saveSelectedIndustry(onSaved: () -> Unit) {
        val selectedIndustry = _uiState.value.selectedIndustry ?: return
        viewModelScope.launch {
            val currentParameters = runCatching {
                filterInteractor.getFilterParameters()
            }.getOrNull() ?: ru.practicum.android.diploma.data.FilterParameters()

            filterInteractor.saveFilterParameters(
                currentParameters.copy(industry = selectedIndustry.name)
            )
            onSaved()
        }
    }
}
