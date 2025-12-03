package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.VacancyOutcome
import ru.practicum.android.diploma.util.ResponseCodes

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DomainMapper
) : VacancyRepository {
    override suspend fun searchVacancies(request: SearchRequest): Flow<SearchOutcome> = flow {
        val response = networkClient.doRequest(request)

        when (response) {
            is SearchResponse -> {
                val outcomeResult = mapper.mapSearchOutcome(response)
                emit(outcomeResult)
            }
            else -> {
                val code = response.result
                val outcome = when (code) {
                    ResponseCodes.NO_CONNECTION -> SearchOutcome.Error(DomainError.NoConnection)
                    ResponseCodes.ERROR_SERVER -> SearchOutcome.Error(DomainError.OtherError)
                    else -> SearchOutcome.Error(DomainError.OtherError)
                }
                emit(outcome)
            }
        }
    }

    override suspend fun loadNextPage(query: String, nextPage: Int): Flow<SearchOutcome> = flow {
        val request = SearchRequest(
            text = query,
            page = nextPage,
        )

        val response = networkClient.doRequest(request)

        when (response) {
            is SearchResponse -> {
                val outcomeResult = mapper.mapSearchOutcome(response)
                emit(outcomeResult)
            }
            else -> {
                val code = response.result
                val outcome = when (code) {
                    ResponseCodes.NO_CONNECTION -> SearchOutcome.Error(DomainError.NoConnection)
                    ResponseCodes.ERROR_SERVER -> SearchOutcome.Error(DomainError.OtherError)
                    else -> SearchOutcome.Error(DomainError.OtherError)
                }
                emit(outcome)
            }
        }
    }

    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome> = flow {
        val response = networkClient.getVacancyById(vacancyId)
        when (response) {
            is VacancyResponse -> emit(mapper.mapVacancyOutcome(response))
            else -> {
                val code = (response as? SearchResponse)?.result ?: ResponseCodes.ERROR_SERVER
                val outcome = when (code) {
                    ResponseCodes.NO_CONNECTION -> VacancyOutcome.Error(DomainError.NoConnection)
                    ResponseCodes.ERROR_SERVER -> VacancyOutcome.Error(DomainError.OtherError)
                    else -> VacancyOutcome.Error(DomainError.OtherError)
                }
                emit(outcome)
            }
        }
    }
}
