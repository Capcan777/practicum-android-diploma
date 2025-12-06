package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.api.IndustryRepository
import ru.practicum.android.diploma.domain.models.IndustriesOutcome

class IndustriesInteractorImpl(private val industryRepository: IndustryRepository) : IndustriesInteractor {
    override suspend fun getFilterIndustries(): Flow<IndustriesOutcome> {
        return industryRepository.getFilterIndustries()
    }
}
