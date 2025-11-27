package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchInteractorImpl(private val vacancyRepository: VacancyRepository) : SearchInteractor {
    override suspend fun searchVacancies(request: SearchRequest): Flow<List<Vacancy>?> {
        return vacancyRepository.searchVacancies(request)
    }
}
