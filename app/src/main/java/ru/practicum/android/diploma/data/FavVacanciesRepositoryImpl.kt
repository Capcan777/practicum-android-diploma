package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.db.converters.FavVacanciesDbConvertor
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.api.FavVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavVacanciesRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val favVacanciesDbConvertor: FavVacanciesDbConvertor
) : FavVacanciesRepository {
    override suspend fun addFavVacancy(vacancy: Vacancy) {
        vacancyDao.addVacancyToFavorites(convertFromVacancy(vacancy))
    }

    override suspend fun removeFavVacancy(vacancy: Vacancy) {
        vacancyDao.deleteVacancyById(convertFromVacancy(vacancy).vacancyId)
    }

    override suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        val count = vacancyDao.isVacancyExists(vacancyId)
        return count > 0
    }

    private fun convertFromVacancy(vacancy: Vacancy): VacancyEntity {
        return favVacanciesDbConvertor.map(vacancy)
    }
}
