package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.DomainMapper
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ResponseCodes

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DomainMapper
) : VacancyRepository {
    override suspend fun searchVacancies(request: SearchRequest): Flow<List<Vacancy>> = flow {
        val response = networkClient.doRequest(request)
        when (response.result) {
            ResponseCodes.SUCCESS -> {
                val vacancies = (response as SearchResponse).vacancies.map { vacancyDto ->
                    mapper.mapVacancy(vacancyDto)
                }
                emit(vacancies)
            }
            else -> {
                // обработать ошибку
                emit(emptyList())
            }
        }
    }
}
