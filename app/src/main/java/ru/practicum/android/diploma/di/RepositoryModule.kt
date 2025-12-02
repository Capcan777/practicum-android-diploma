package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FavVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.data.db.converters.FavVacanciesDbConvertor
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.FavVacanciesRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {

    single<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }

    single<FavVacanciesRepository> {
        FavVacanciesRepositoryImpl(get(), get())
    }

    single { DomainMapper() }

    single { FavVacanciesDbConvertor() }

}
