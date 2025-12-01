package ru.practicum.android.diploma.data.dto

class VacancyResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDto? = null,
    val address: AddressDto? = null,
    val experience: ExperienceDto? = null,
    val schedule: ScheduleDto? = null,
    val employment: EmploymentDto? = null,
    val contacts: ContactsDto? = null,
    val employer: EmployerDto,
    val area: FilterAreaDto,
    val skills: List<String>? = null,
    val url: String? = null,
    val industry: FilterIndustryDto
) : Response()
