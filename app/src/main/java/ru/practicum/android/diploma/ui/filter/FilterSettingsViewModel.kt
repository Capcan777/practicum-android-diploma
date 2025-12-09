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
            _uiState.value = parameters.toFilterSettingsState()
        }
    }

    private fun saveFilterState() {
        viewModelScope.launch {
            filterInteractor.saveFilterParameters(_uiState.value.toFilterParameters())
        }
    }

    private fun updateState(update: (FilterSettingsState) -> FilterSettingsState) {
        _uiState.update { currentState ->
            update(currentState).also { saveFilterState() }
        }
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
        updateState { it.copy(industry = newIndustry) }
    }

    fun onPlaceOfWorkChanged(newPlaceOfWork: String) {
        updateState { it.copy(placeOfWork = newPlaceOfWork) }
    }

    fun applyFilters() {
        viewModelScope.launch {
            val params = _uiState.value.toFilterParameters()
            filterInteractor.saveFilterParameters(params)
        }
    }

    private fun FilterSettingsState.toFilterParameters(): FilterParameters {
        return FilterParameters(
            salary = this.salary,
            industry = this.industry,
            placeOfWork = this.placeOfWork,
            hideWithoutSalary = this.hideWithoutSalary
        )
    }

    private fun FilterParameters.toFilterSettingsState(): FilterSettingsState {
        return FilterSettingsState(
            salary = this.salary,
            industry = this.industry,
            placeOfWork = this.placeOfWork,
            hideWithoutSalary = this.hideWithoutSalary
        )
    }

    fun applyFilters() {
        // Фильтры уже сохраняются автоматически при изменении,
        // но явно сохраняем текущее состояние для гарантии
        saveFilterState()
    }
}
