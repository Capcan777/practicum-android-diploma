package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.IndustryRepository
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.IndustriesOutcome
import ru.practicum.android.diploma.util.ResponseCodes

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DomainMapper
) : IndustryRepository {
    override suspend fun getFilterIndustries(): Flow<IndustriesOutcome> = flow {
        val response = networkClient.doRequest(IndustriesRequest())

        when (response) {
            is IndustriesResponse -> {
                val outcomeResult = mapper.mapIndustriesOutcome(response)
                emit(outcomeResult)
            }
            else -> {
                val code = response.result
                val outcome = when (code) {
                    ResponseCodes.NO_CONNECTION -> IndustriesOutcome.Error(DomainError.NoConnection)
                    ResponseCodes.ERROR_SERVER -> IndustriesOutcome.Error(DomainError.OtherError)
                    else -> IndustriesOutcome.Error(DomainError.OtherError)
                }
                emit(outcome)
            }
        }
    }
}
