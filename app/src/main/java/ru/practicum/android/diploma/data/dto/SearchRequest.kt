package ru.practicum.android.diploma.data.dto

data class SearchRequest(
    val industry: Int? = null,
    val text: String? = null,
    val salary: Int? = null,
    val page: Int,
    val onlyWithSalary: Boolean = false
)
