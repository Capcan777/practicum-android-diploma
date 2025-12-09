package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.FilterParameters
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.ui.filter.state.FilterSettingsState

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterSettingsState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFilterState()
    }

    private fun loadFilterState() {
        viewModelScope.launch {
            val parameters = filterInteractor.getFilterParameters()
            _uiState.value = FilterSettingsState(
                salary = parameters.salary,
                industry = parameters.industry,
                placeOfWork = parameters.placeOfWork,
                hideWithoutSalary = parameters.hideWithoutSalary
            )
        }
    }

    private fun saveFilterState() {
        viewModelScope.launch {
            val currentState = _uiState.value
            filterInteractor.saveFilterParameters(
                FilterParameters(
                    salary = currentState.salary,
                    industry = currentState.industry,
                    placeOfWork = currentState.placeOfWork,
                    hideWithoutSalary = currentState.hideWithoutSalary
                )
            )
        }
    }

    fun onSalaryChanged(newSalary: String) {
        if (newSalary.all { it.isDigit() }) {
            _uiState.update { currentState ->
                currentState.copy(salary = newSalary)
            }
            saveFilterState()
        }
    }

    fun onCheckboxChanged(isChecked: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(hideWithoutSalary = isChecked)
        }
        saveFilterState()
    }

    fun clearSalary() {
        _uiState.update { currentState ->
            currentState.copy(salary = "")
        }
        saveFilterState()
    }

    fun onIndustryClick(navigateTo: () -> Unit) {
        if (uiState.value.industry.isNotEmpty()) {
            clearIndustry()
        } else {
            navigateTo() // Выполняем навигацию, переданную из UI
        }
    }

    private fun clearIndustry() {
        _uiState.update { currentState ->
            currentState.copy(industry = "")
        }
        saveFilterState()
    }

    fun resetFilters() {
        _uiState.value = FilterSettingsState() // Просто создаем новое, пустое состояние
        viewModelScope.launch {
            filterInteractor.clearFilterParameters()
        }
    }

    // Добавить методы для обновления industry и placeOfWork, когда экраны выбора будут готовы
    fun onIndustryChanged(newIndustry: String) {
        _uiState.update { currentState ->
            currentState.copy(industry = newIndustry)
        }
        saveFilterState()
    }

    fun onPlaceOfWorkChanged(newPlaceOfWork: String) {
        _uiState.update { currentState ->
            currentState.copy(placeOfWork = newPlaceOfWork)
        }
        saveFilterState()
    }

    fun applyFilters() {
        // Фильтры уже сохраняются автоматически при изменении,
        // но явно сохраняем текущее состояние для гарантии
        saveFilterState()
    }
}
