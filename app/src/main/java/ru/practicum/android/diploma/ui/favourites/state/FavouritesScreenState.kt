package ru.practicum.android.diploma.ui.favourites.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class FavoritesScreenState {
    object Loading : FavoritesScreenState()
    object Empty : FavoritesScreenState()
    data class Content(val items: List<Vacancy>) : FavoritesScreenState()
    data class Error(val message: String) : FavoritesScreenState()
}
