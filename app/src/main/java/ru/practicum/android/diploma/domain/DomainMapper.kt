package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.dto.VacancyItemDto
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.Vacancy

class DomainMapper {
    fun mapVacancy(dto: VacancyItemDto): Vacancy {
        return Vacancy(
            id = dto.id,
            title = dto.name,
            description = dto.description,
            salary = mapSalary(dto.salary),
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

    fun mapSearchResult(response: SearchResponse): SearchResult {
        return SearchResult(
            vacancies = response.items.map { vacancyDto ->
                mapVacancy(vacancyDto)
            },
            currentPage = response.page,
            totalPages = response.pages,
            found = response.found
        )
    }
}
