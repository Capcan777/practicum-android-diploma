package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.SearchOutcome

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DomainMapper
) : VacancyRepository {
    override suspend fun searchVacancies(request: SearchRequest): Flow<SearchOutcome> = flow {
        val response = networkClient.doRequest(request)

        val searchResponse = response as SearchResponse
        val outcomeResult = mapper.mapSearchOutcome(searchResponse)
        emit(outcomeResult)
    }

    override suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchOutcome> = flow {
        val request = SearchRequest(
            text = query,
            page = nextPage,
        )

        val response = networkClient.doRequest(request)
        val searchResponse = response as SearchResponse
        val searchResult = mapper.mapSearchOutcome(searchResponse)
        emit(searchResult)

    }
}
