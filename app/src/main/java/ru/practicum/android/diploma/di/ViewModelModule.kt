package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.details.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.favourites.FavouritesViewModel
import ru.practicum.android.diploma.ui.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.ui.industry.IndustrySelectionViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.util.ResourceProvider

val viewModelModule = module {
    viewModel {
        SearchViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { (vacancyId: String) ->
        VacancyDetailsViewModel(
            vacancyId,
            get(),
            get(),
            get()
        )
    }
    viewModel {
        FavouritesViewModel(get())
    }
    viewModel {
        FilterSettingsViewModel(get())
    }
    viewModel {
        IndustrySelectionViewModel(get())
    }
    single { ResourceProvider(androidContext()) }
}
