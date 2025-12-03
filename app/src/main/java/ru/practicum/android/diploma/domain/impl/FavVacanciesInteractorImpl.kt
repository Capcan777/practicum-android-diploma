package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.FavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.FavVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavVacanciesInteractorImpl(
    private val favVacanciesRepository: FavVacanciesRepository
) : FavVacanciesInteractor {
    override suspend fun addFavVacancy(vacancy: Vacancy) {
        favVacanciesRepository.addFavVacancy(vacancy)
    }

    override suspend fun removeFavVacancy(vacancy: Vacancy) {
        favVacanciesRepository.removeFavVacancy(vacancy)
    }

    override suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        return favVacanciesRepository.isVacancyFavorite(vacancyId)
    }

    override fun getFavVacancies(): Flow<List<Vacancy>> {
        return favVacanciesRepository.getFavVacancies()
    }
}
