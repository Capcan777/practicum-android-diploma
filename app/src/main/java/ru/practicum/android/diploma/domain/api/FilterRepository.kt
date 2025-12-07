package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.FilterParameters

interface FilterRepository {
    suspend fun saveFilterParameters(parameters: FilterParameters)
    suspend fun getFilterParameters(): FilterParameters
    suspend fun clearFilterParameters()
}
