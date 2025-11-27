package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.search.SearchViewModel

val viewModelModule = module {

    // добавить инициализацию viewmodel'ов для каждого экрана

    viewModel {
        SearchViewModel(get())
    }

}
