package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.details.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel { (vacancyId: String) ->
        VacancyDetailsViewModel(vacancyId, get(), get())
    }

}
