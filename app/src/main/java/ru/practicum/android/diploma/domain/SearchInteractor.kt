package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchInteractor {
    suspend fun searchVacancies(request: SearchRequest) : Flow<List<Vacancy>?>
}
