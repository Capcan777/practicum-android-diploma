package ru.practicum.android.diploma.data.dto

class SearchResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val vacancies: List<VacancyItemDto>
): Response()
