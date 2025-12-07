package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.FilterParameters
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository

class FilterInteractorImpl(
    private val filterRepository: FilterRepository
) : FilterInteractor {

    override suspend fun saveFilterParameters(parameters: FilterParameters) {
        filterRepository.saveFilterParameters(parameters)
    }

    override suspend fun getFilterParameters(): FilterParameters {
        return filterRepository.getFilterParameters()
    }

    override suspend fun clearFilterParameters() {
        filterRepository.clearFilterParameters()
    }
}
