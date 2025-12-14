package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.practicum.android.diploma.data.FilterParameters
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository

class FilterInteractorImpl(
    private val filterRepository: FilterRepository
) : FilterInteractor {

    private val _filterUpdates = MutableSharedFlow<FilterParameters>(replay = 1)
    override val filterUpdates: SharedFlow<FilterParameters> = _filterUpdates.asSharedFlow()

    override suspend fun saveFilterParameters(parameters: FilterParameters, apply: Boolean) {
        filterRepository.saveFilterParameters(parameters)
        if (apply) {
            _filterUpdates.emit(parameters)
        }
    }

    override suspend fun getFilterParameters(): FilterParameters {
        return filterRepository.getFilterParameters()
    }

    override suspend fun clearFilterParameters() {
        filterRepository.clearFilterParameters()
        _filterUpdates.emit(FilterParameters())
    }
}
