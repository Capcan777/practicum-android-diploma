package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.FavVacanciesInteractor
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.SharingInteractor
import ru.practicum.android.diploma.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.impl.FavVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<FavVacanciesInteractor> {
        FavVacanciesInteractorImpl(get())
    }

    single<FilterInteractor> {
        FilterInteractorImpl(get())
    }
    
    single<IndustriesInteractor> {
        IndustriesInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get(), get())
    }
}
