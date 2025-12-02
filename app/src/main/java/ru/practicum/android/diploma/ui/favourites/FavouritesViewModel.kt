package ru.practicum.android.diploma.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FavVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.favourites.state.FavoritesScreenState

class FavouritesViewModel(
    private val favVacanciesInteractor: FavVacanciesInteractor
) : ViewModel() {

    private var vacancies: List<Vacancy> = emptyList()

    private val _screenState = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Loading)
    val screenState: StateFlow<FavoritesScreenState> = _screenState.asStateFlow()

    init {
        _screenState.value = FavoritesScreenState.Loading
        updateFavouriteList()
    }

    fun updateFavouriteList() {
        viewModelScope.launch(Dispatchers.IO) {
            favVacanciesInteractor
                .getFavVacancies()
                .collect { vacancies ->
                    loadResult(vacancies)
                }
        }
    }

    fun loadResult(vacancyList: List<Vacancy>) {
        vacancies = vacancyList
        if (vacancies.isEmpty()) {
            _screenState.value = FavoritesScreenState.Empty
        } else {
            _screenState.value = FavoritesScreenState.Content(vacancies)
        }
    }
}
