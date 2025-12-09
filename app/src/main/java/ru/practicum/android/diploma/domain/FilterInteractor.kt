package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.SharedFlow
import ru.practicum.android.diploma.data.FilterParameters

interface FilterInteractor {
    suspend fun saveFilterParameters(parameters: FilterParameters)
    suspend fun getFilterParameters(): FilterParameters
    suspend fun clearFilterParameters()

    val filterUpdates: SharedFlow<FilterParameters>
}
