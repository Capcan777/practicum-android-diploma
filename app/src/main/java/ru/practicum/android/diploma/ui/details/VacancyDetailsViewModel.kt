package ru.practicum.android.diploma.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
        loadVacancyDetails()
    }

    private fun loadVacancyDetails() {
        viewModelScope.launch {
            _screenState.value = VacancyDetailsScreenState.Loading

            try {
                searchInteractor.getVacancyById(vacancyId).collect { vacancyOutcome ->
                    when (vacancyOutcome) {
                        is VacancyOutcome.Success -> {
                            handleVacancy(vacancyOutcome)
                        }

                        is VacancyOutcome.Error -> {
                            handleError(vacancyOutcome)
                        }
                    }
                }
            } catch (e: HttpException) {
                Log.e("VacancyDetailsViewModel", "HTTP error: ${e.code()}", e)
                _screenState.value = VacancyDetailsScreenState.Error.ServerError
            }
        }
    }

    private fun handleVacancy(vacancyOutcome: VacancyOutcome.Success) {
        _screenState.value = VacancyDetailsScreenState.Content(
            vacancy = vacancyOutcome.vacancy
        )
    }

    private fun handleError(vacancyOutcome: VacancyOutcome.Error) {
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
