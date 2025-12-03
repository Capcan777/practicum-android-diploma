package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        val entity = convertFromVacancy(vacancy)
        val exists = vacancyDao.isVacancyExists(entity.vacancyId) > 0
        if (!exists) {
            vacancyDao.addVacancyToFavorites(entity)
        }
    }

    override suspend fun removeFavVacancy(vacancy: Vacancy) {
        vacancyDao.deleteVacancyById(convertFromVacancy(vacancy).vacancyId)
    }

    override suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        val count = vacancyDao.isVacancyExists(vacancyId)
        return count > 0
    }

    override suspend fun getFavVacancies(): Flow<List<Vacancy>> = flow {
        val vacancies = vacancyDao.getFavoriteVacancies()
        emit(convertFromFavVacanciesEntity(vacancies))
    }

    private fun convertFromVacancy(vacancy: Vacancy): VacancyEntity {
        return favVacanciesDbConvertor.map(vacancy)
    }

    private fun convertFromFavVacanciesEntity(vacancies: List<VacancyEntity>): List<Vacancy> {
        return vacancies.map { vacancy -> favVacanciesDbConvertor.map(vacancy) }
    }
}
