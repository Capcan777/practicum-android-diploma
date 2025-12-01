package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val title: String,
    val description: String,
    val salary: SalaryRange?,
    val experience: Experience?,
    val company: Employer,
    val location: String?,
    val industry: Industry,
    val address: Address? = null,
    val contacts: Contacts? = null,
    val employment: Employment? = null,
    val schedule: Schedule? = null,
    val skills: List<String> = emptyList(),
    val url: String? = null
)
