package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.domain.models.VacancyOutcome

interface VacancyRepository {
    suspend fun searchVacancies(request: SearchRequest): Flow<SearchOutcome>

    suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchOutcome>

    suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome>
}
