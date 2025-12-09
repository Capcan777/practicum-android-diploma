package ru.practicum.android.diploma.domain

import android.util.Log
import ru.practicum.android.diploma.data.dto.AddressDto
import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.EmploymentDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.ScheduleDto
import ru.practicum.android.diploma.data.dto.SearchResponse
import ru.practicum.android.diploma.data.dto.VacancyItemDto
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.IndustriesOutcome
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.SearchOutcome
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyOutcome
import ru.practicum.android.diploma.util.ResponseCodes

class DomainMapper {
    fun mapVacancy(dto: VacancyItemDto): Vacancy {
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

    private fun mapSalary(dto: SalaryDto?): SalaryRange {
        return SalaryRange(
            from = dto?.from,
            to = dto?.to,
            currency = dto?.currency
        )
    }

    private fun mapEmployer(dto: EmployerDto): Employer {
        return Employer(
            id = dto.id,
            name = dto.name,
            logoUrl = dto.logo
        )
    }

    private fun mapIndustry(dto: FilterIndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapExperience(dto: ExperienceDto?): Experience {
        return Experience(
            id = dto?.id,
            name = dto?.name
        )
    }

    private fun mapAddress(dto: AddressDto?): Address? {
        if (dto == null) return null
        return Address(
            city = dto.city,
            street = dto.street,
            building = dto.building,
            fullAddress = dto.fullAddress
        )
    }

    private fun mapContacts(dto: ContactsDto): Contacts {
        val phones = dto.phone?.mapNotNull { phoneString ->
            phoneString.takeIf { it.isNotBlank() }?.let {
                Phone(
                    comment = null,
                    formatted = it
                )
            }
        }
        return Contacts(
            name = dto.name,
            email = dto.email,
            phones = if (phones.isNullOrEmpty()) null else phones
        )
    }

    private fun mapEmployment(dto: EmploymentDto): Employment {
        return Employment(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapSchedule(dto: ScheduleDto): Schedule {
        return Schedule(
            id = dto.id,
            name = dto.name
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

    fun mapIndustriesOutcome(response: IndustriesResponse): IndustriesOutcome {
        if (response.result == ResponseCodes.SUCCESS) {
            val allIndustries = mutableListOf<Industry>()

            response.items.forEach { parentIndustry ->
                allIndustries.add(mapIndustry(parentIndustry))

                parentIndustry.industries?.forEach { childIndustry ->
                    allIndustries.add(mapIndustry(childIndustry))
                }
            }

            return IndustriesOutcome.IndustriesResult(
                industries = allIndustries
            )
        } else {
            return when (response.result) {
                ResponseCodes.NO_CONNECTION -> {
                    IndustriesOutcome.Error(DomainError.NoConnection)
                }

                ResponseCodes.ERROR_SERVER -> {
                    IndustriesOutcome.Error(DomainError.OtherError)
                }

                else -> {
                    IndustriesOutcome.Error(DomainError.OtherError)
                }
            }
        }
    }

    fun mapFromVacancyResponse(dto: VacancyResponse): Vacancy {
        Log.d(
            "DomainMapper",
            "mapFromVacancyResponse: id=${dto.id}, contacts=${dto.contacts}, contacts.phone=${dto.contacts?.phone}"
        )
        return Vacancy(
            id = dto.id,
            title = dto.name,
            description = dto.description,
            salary = mapSalary(dto.salary),
            experience = mapExperience(dto.experience),
            company = mapEmployer(dto.employer),
            location = dto.address?.fullAddress ?: dto.area.name,
            industry = mapIndustry(dto.industry),
            address = dto.address?.let { mapAddress(it) },
            contacts = dto.contacts?.let { mapContacts(it) },
            employment = dto.employment?.let { mapEmployment(it) },
            schedule = dto.schedule?.let { mapSchedule(it) },
            skills = dto.skills ?: emptyList(),
            url = dto.url
        )
    }
}
