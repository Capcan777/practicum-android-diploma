package ru.practicum.android.diploma.data.dto

data class VacancyItemDto(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDto? = null,
    val experience: ExperienceDto? = null,
    val employer: EmployerDto,
    val industry: FilterIndustryDto,
    val area: FilterAreaDto
)
