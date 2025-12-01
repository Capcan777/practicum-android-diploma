package ru.practicum.android.diploma.domain

import android.util.Log
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.dto.VacancyItemDto
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.VacancyOutcome
import ru.practicum.android.diploma.util.ResponseCodes

class DomainMapper {
    fun mapVacancy(dto: VacancyItemDto): Vacancy {
        Log.d("DomainMapper", "Mapping Vacancy: ${dto.name}")
        return Vacancy(
            id = dto.id,
            title = dto.name,
            description = dto.description,
            salary = mapSalary(dto.salary),
            experience = mapExperience(dto.experience),
            company = mapEmployer(dto.employer),
            location = dto.area.name,
            industry = mapIndustry(dto.industry)
        )
    }

    fun mapSalary(dto: SalaryDto?): SalaryRange {
        return SalaryRange(
            from = dto?.from,
            to = dto?.to,
            currency = dto?.currency
        )
    }

    fun mapEmployer(dto: EmployerDto): Employer {
        return Employer(
            id = dto.id,
            name = dto.name,
            logoUrl = dto.logo
        )
    }

    fun mapIndustry(dto: FilterIndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }

    fun mapExperience(dto: ExperienceDto?): Experience {
        return Experience(
            id = dto?.id,
            name = dto?.name
        )
    }

    fun mapSearchOutcome(response: SearchResponse): SearchOutcome {
        if (response.result == ResponseCodes.SUCCESS) {
            return SearchOutcome.SearchResult(
                vacancies = response.items.map { vacancyDto ->
                    mapVacancy(vacancyDto)
                },
                currentPage = response.page,
                totalPages = response.pages,
                found = response.found
            )
        } else {
            return when (response.result) {
                ResponseCodes.NO_CONNECTION -> {
                    SearchOutcome.Error(DomainError.NoConnection)
                }
                ResponseCodes.ERROR_SERVER -> {
                    SearchOutcome.Error(DomainError.OtherError)
                }
                else -> {
                    SearchOutcome.Error(DomainError.OtherError)
                }
            }
        }
    }

    fun mapVacancyOutcome(response: VacancyResponse): VacancyOutcome {
        if (response.result == ResponseCodes.SUCCESS) {
            return VacancyOutcome.Success(
                vacancy = mapFromVacancyResponse(response)
            )
        } else {
            return when (response.result) {
                ResponseCodes.NO_CONNECTION -> {
                    VacancyOutcome.Error(DomainError.NoConnection)
                }
                ResponseCodes.ERROR_SERVER -> {
                    VacancyOutcome.Error(DomainError.OtherError)
                }
                else -> {
                    VacancyOutcome.Error(DomainError.OtherError)
                }
            }
        }
    }

    fun mapFromVacancyResponse(dto: VacancyResponse): Vacancy {
        Log.d("DomainMapper", "Mapping Vacancy: ${dto.name}")
        return Vacancy(
            id = dto.id,
            title = dto.name,
            description = dto.description,
            salary = mapSalary(dto.salary),
            experience = mapExperience(dto.experience),
            company = mapEmployer(dto.employer),
            location = dto.area.name,
            industry = mapIndustry(dto.industry)
        )
    }
}
