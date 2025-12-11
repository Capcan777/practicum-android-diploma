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

    // Добавляем флаг для отслеживания изменений
    private var hasUnsavedChanges = false

    init {
        loadFilterState()
    }

    fun loadFilterState() {
        viewModelScope.launch {
            val parameters = filterInteractor.getFilterParameters()
            _uiState.value = parameters.toFilterSettingsState()
        }
    }

    private fun saveFilterState() {
        viewModelScope.launch {
            filterInteractor.saveFilterParameters(_uiState.value.toFilterParameters())
            hasUnsavedChanges = false // Сбрасываем флаг после сохранения
        }
    }

    private fun updateState(update: (FilterSettingsState) -> FilterSettingsState) {
        _uiState.update { currentState ->
            update(currentState)
        }
        hasUnsavedChanges = true // Отмечаем, что есть несохраненные изменения
    }

    fun onSalaryChanged(newSalary: String) {
        if (newSalary.all { it.isDigit() }) {
            updateState { it.copy(salary = newSalary) }
        }
    }

    fun onCheckboxChanged(isChecked: Boolean) {
        updateState { it.copy(hideWithoutSalary = isChecked) }
    }

    fun clearSalary() {
        updateState { it.copy(salary = "") }
    }

    fun onIndustryClick(navigateTo: () -> Unit) {
        navigateTo()
    }

    fun clearIndustry() {
        updateState { it.copy(industry = "") }
    }

    fun resetFilters() {
        _uiState.value = FilterSettingsState()
        hasUnsavedChanges = true
        viewModelScope.launch {
            filterInteractor.clearFilterParameters()
        }
    }

    fun onIndustryChanged(newIndustry: String) {
        updateState { it.copy(industry = newIndustry) }
    }

    // Обновляем метод applyFilters
    fun applyFilters() {
        saveFilterState() // Просто сохраняем текущее состояние
    }

    // Новый метод для сохранения при выходе
    fun onBackPressed() {
        if (hasUnsavedChanges) {
            saveFilterState()
        }
    }

    private fun FilterSettingsState.toFilterParameters(): FilterParameters {
        return FilterParameters(
            salary = this.salary,
            industry = this.industry,
            hideWithoutSalary = this.hideWithoutSalary
        )
    }

    private fun FilterParameters.toFilterSettingsState(): FilterSettingsState {
        return FilterSettingsState(
            salary = this.salary,
            industry = this.industry,
            hideWithoutSalary = this.hideWithoutSalary
        )
    }
}
