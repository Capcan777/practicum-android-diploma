package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.domain.api.FilterRepository

class FilterRepositoryImpl(
    private val filterStorage: FilterStorage
) : FilterRepository {

    override suspend fun saveFilterParameters(parameters: FilterParameters) {
        filterStorage.saveFilterParameters(
            FilterParameters(
                salary = parameters.salary,
                industry = parameters.industry,
                placeOfWork = parameters.placeOfWork,
                hideWithoutSalary = parameters.hideWithoutSalary
            )
        )
    }

    override suspend fun getFilterParameters(): FilterParameters {
        val dataParameters = filterStorage.getFilterParameters()
        return FilterParameters(
            salary = dataParameters.salary,
            industry = dataParameters.industry,
            placeOfWork = dataParameters.placeOfWork,
            hideWithoutSalary = dataParameters.hideWithoutSalary
        )
    }

    override suspend fun clearFilterParameters() {
        filterStorage.clearFilterParameters()
    }
}
