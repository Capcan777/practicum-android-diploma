package ru.practicum.android.diploma.domain.models

sealed class SearchOutcome {
    data class SearchResult(
        val found: Int,
        val totalPages: Int,
        val currentPage: Int,
        val vacancies: List<Vacancy>,
    ): SearchOutcome()
    data class Error(
        val type: DomainError
    ): SearchOutcome()
}
