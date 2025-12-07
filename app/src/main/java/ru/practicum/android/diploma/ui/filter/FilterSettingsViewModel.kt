package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.practicum.android.diploma.data.FilterStorage
import ru.practicum.android.diploma.ui.filter.state.FilterSettingsState

class FilterSettingsViewModel(
    private val filterStorage: FilterStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(loadFilterState())
    val uiState = _uiState.asStateFlow()

    private fun loadFilterState(): FilterSettingsState {
        val parameters = filterStorage.getFilterParameters()
        return FilterSettingsState(
            salary = parameters.salary,
            industry = parameters.industry,
            placeOfWork = parameters.placeOfWork,
            hideWithoutSalary = parameters.hideWithoutSalary
        )
    }

    private fun saveFilterState() {
        val currentState = _uiState.value
        filterStorage.saveFilterParameters(
            ru.practicum.android.diploma.data.FilterParameters(
                salary = currentState.salary,
                industry = currentState.industry,
                placeOfWork = currentState.placeOfWork,
                hideWithoutSalary = currentState.hideWithoutSalary
            )
        )
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

    fun resetFilters() {
        _uiState.value = FilterSettingsState() // Просто создаем новое, пустое состояние
        filterStorage.clearFilterParameters()
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
}
