package ru.practicum.android.diploma.domain.models

sealed class IndustriesOutcome {
    data class IndustriesResult(
        val industries: List<Industry>,
    ) : IndustriesOutcome()
    data class Error(
        val type: DomainError
    ) : IndustriesOutcome()
}
