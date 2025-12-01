package ru.practicum.android.diploma.data.dto

class VacancyResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDto? = null,
    val experience: ExperienceDto? = null,
    val employer: EmployerDto,
    val industry: FilterIndustryDto,
    val area: FilterAreaDto
) : Response()
