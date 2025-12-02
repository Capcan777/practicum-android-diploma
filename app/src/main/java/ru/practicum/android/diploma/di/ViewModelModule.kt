package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.details.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.util.ResourceProvider

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel { (vacancyId: String) ->
        VacancyDetailsViewModel(
            vacancyId,
            get(),
            get(),
            get()
        )
    }

    single { ResourceProvider(androidContext()) }

}
