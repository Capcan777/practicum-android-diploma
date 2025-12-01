package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyOutcome

class SearchInteractorImpl(private val vacancyRepository: VacancyRepository) : SearchInteractor {

    override suspend fun searchVacancies(request: SearchRequest): Flow<SearchOutcome> {
        return vacancyRepository.searchVacancies(request)
    }

    override suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchOutcome> {
        return vacancyRepository.loadNextPage(query, nextPage)
    }

    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome> {
        return vacancyRepository.getVacancyById(vacancyId)
    }
}
