package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val title: String,
    val description: String,
    val salary: SalaryRange?,
    val company: Employer,
    val location: String?,
    val industry: Industry
)
