package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustriesOutcome

interface IndustriesInteractor {
    suspend fun getFilterIndustries(): Flow<IndustriesOutcome>
}
