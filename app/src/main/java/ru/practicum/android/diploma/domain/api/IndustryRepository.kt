package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustriesOutcome

interface IndustryRepository {
    suspend fun getFilterIndustries(): Flow<IndustriesOutcome>
}
