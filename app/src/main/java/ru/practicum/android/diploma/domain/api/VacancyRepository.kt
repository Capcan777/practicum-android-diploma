package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.models.SearchOutcome

interface VacancyRepository {
    suspend fun searchVacancies(request: SearchRequest): Flow<SearchOutcome>

    suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchOutcome>
}
