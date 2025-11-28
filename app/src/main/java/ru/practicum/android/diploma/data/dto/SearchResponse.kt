package ru.practicum.android.diploma.data.dto

class SearchResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyItemDto>
) : Response()
