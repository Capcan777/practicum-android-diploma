package ru.practicum.android.diploma.ui.favourites.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class FavouritesScreenState {
    object Loading : FavouritesScreenState()
    object Empty : FavouritesScreenState()
    data class Content(val items: List<Vacancy>) : FavouritesScreenState()
    data class Error(val message: String) : FavouritesScreenState()
}
