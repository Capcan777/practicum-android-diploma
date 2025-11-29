package ru.practicum.android.diploma.domain.models

data class SearchResult(
    val found: Int,
    val totalPages: Int,
    val currentPage: Int,
    val vacancies: List<Vacancy>
)
