package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.models.SearchResult

interface VacancyRepository {
    suspend fun searchVacancies(request: SearchRequest): Flow<SearchResult?>

    suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchResult?>
}
