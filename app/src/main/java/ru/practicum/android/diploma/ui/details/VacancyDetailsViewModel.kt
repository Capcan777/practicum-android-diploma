package ru.practicum.android.diploma.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.VacancyOutcome
import ru.practicum.android.diploma.ui.details.state.VacancyDetailsScreenState

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyDetailsScreenState>(VacancyDetailsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        Log.d("VacancyDetailsViewModel", "Initialized with vacancyId: $vacancyId")

        loadVacancyDetails()
    }

    private fun loadVacancyDetails() {
        viewModelScope.launch {
            _screenState.value = VacancyDetailsScreenState.Loading

            Log.d("VacancyDetailsViewModel", "Loading vacancy details for ID: $vacancyId")
            try {
                searchInteractor.getVacancyById(vacancyId).collect { vacancyOutcome ->
                    Log.d("VacancyDetailsViewModel", "Collecting outcome")
                    when (vacancyOutcome) {
                        is VacancyOutcome.Success -> {
                            Log.d("VacancyDetailsViewModel", "Success: ${vacancyOutcome.vacancy.title}")
                            handleVacancy(vacancyOutcome)
                        }

                        is VacancyOutcome.Error -> {
                            Log.d("VacancyDetailsViewModel", "Error: ${vacancyOutcome.type}")
                            handleError(vacancyOutcome)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("VacancyDetailsViewModel", "Error: ${e.message}")
                _screenState.value = VacancyDetailsScreenState.Error.ServerError
            }
        }
    }


    private fun handleVacancy(vacancyOutcome: VacancyOutcome.Success) {
        Log.d("VacancyDetailsViewModel", "Vacancy loaded: ${vacancyOutcome.vacancy}")
        _screenState.value = VacancyDetailsScreenState.Content(
            vacancy = vacancyOutcome.vacancy
        )
    }

    private fun handleError(vacancyOutcome: VacancyOutcome.Error) {
        Log.e("VacancyDetailsViewModel", "Error loading vacancy: ${vacancyOutcome.type}")
        when (vacancyOutcome.type) {
            DomainError.NotFound -> {
                _screenState.value = VacancyDetailsScreenState.Error.NotFound
            }

            DomainError.OtherError -> {
                _screenState.value = VacancyDetailsScreenState.Error.ServerError
            }

            else -> {
                _screenState.value = VacancyDetailsScreenState.Error.ServerError
            }
        }
    }
}
