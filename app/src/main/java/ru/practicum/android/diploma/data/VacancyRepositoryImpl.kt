package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.util.ResponseCodes

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DomainMapper
) : VacancyRepository {
    override suspend fun searchVacancies(request: SearchRequest): Flow<SearchResult?> = flow {
        val response = networkClient.doRequest(request)
        when (response.result) {
            ResponseCodes.SUCCESS -> {
                val searchResponse = response as SearchResponse
                val searchResult = mapper.mapSearchResult(searchResponse)
                emit(searchResult)
            }

            else -> {
                // возвращаем null при ошибке (изменить желательно)
                emit(null)
            }
        }
    }

    override suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchResult?> = flow {
        val request = SearchRequest(
            text = query,
            page = nextPage,
        )

        val response = networkClient.doRequest(request)
        when (response.result) {
            ResponseCodes.SUCCESS -> {
                val searchResponse = response as SearchResponse
                val searchResult = mapper.mapSearchResult(searchResponse)
                emit(searchResult)
            }

            else -> {
                // возвращаем null при ошибке (изменить желательно)
                emit(null)
            }
        }
    }
}
