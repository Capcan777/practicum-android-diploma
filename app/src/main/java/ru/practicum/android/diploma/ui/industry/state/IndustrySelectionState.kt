package ru.practicum.android.diploma.ui.industry.state

import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.Industry

data class IndustrySelectionState(
    val industries: List<Industry> = emptyList(),
    val filteredIndustries: List<Industry> = emptyList(),
    val searchQuery: String = "",
    val selectedIndustry: Industry? = null,
    val isUserSelected: Boolean = false,
    val isLoading: Boolean = false,
    val error: DomainError? = null
)
