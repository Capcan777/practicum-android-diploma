package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.Vacancy

class FavVacanciesDbConvertor {

    fun map(vacancyEntity: VacancyEntity): Vacancy {
        return Vacancy(
            id = vacancyEntity.vacancyId,
            title = vacancyEntity.title,
            description = vacancyEntity.description,
            salary = toSalaryRange(vacancyEntity.salary),
            experience = parseExperienceString(vacancyEntity.experience),
            company = parseEmployerString(vacancyEntity.company),
            location = vacancyEntity.location.takeIf { it.isNotBlank() },
            industry = parseIndustryString(vacancyEntity.industry)
        )
    }

    fun map(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            vacancyId = vacancy.id,
            title = vacancy.title,
            description = vacancy.description,
            salary = fromSalaryRange(vacancy.salary),
            experience = experienceToString(vacancy.experience),
            company = employerToString(vacancy.company),
            location = vacancy.location ?: "",
            industry = industryToString(vacancy.industry)
        )
    }

    fun toSalaryRange(salary: String?): SalaryRange? {
        if (salary.isNullOrBlank()) return null

        val cleaned = salary
            .replace("\u00A0", " ")
            .replace(",", "")
            .trim()

        val numbers = Regex("""\d+""").findAll(cleaned).mapNotNull { it.value.toIntOrNull() }.toList()
        val currency = Regex("""RUB|USD|EUR|HKD""", RegexOption.IGNORE_CASE).find(cleaned)?.value

        val from = when {
            cleaned.startsWith("от", ignoreCase = true) && numbers.isNotEmpty() -> numbers.first()
            numbers.isNotEmpty() -> numbers.first()
            else -> null
        }

        val to = when {
            cleaned.startsWith("до", ignoreCase = true) && numbers.isNotEmpty() -> numbers.first()
            numbers.size >= 2 -> numbers[1]
            else -> null
        }

        return SalaryRange(from = from, to = to, currency = currency)
    }

    fun fromSalaryRange(salary: SalaryRange?): String {
        if (salary == null) return ""
        val from = salary.from
        val to = salary.to
        val currency = salary.currency?.trim() ?: ""

        return when {
            from != null && to != null -> "${from}-${to}${if (currency.isNotBlank()) " $currency" else ""}"
            from != null -> "от${from}${if (currency.isNotBlank()) " $currency" else ""}"
            to != null -> "до${to}${if (currency.isNotBlank()) " $currency" else ""}"
            else -> ""
        }
    }

    fun parseExperienceString(s: String?): Experience? {
        if (s.isNullOrBlank()) return null
        val parts = s.split("|", limit = 2)
        return if (parts.size == 2) Experience(id = parts[0].takeIf { it.isNotBlank() }, name = parts[1].takeIf { it.isNotBlank() })
        else Experience(id = null, name = s)
    }

    fun experienceToString(exp: Experience?): String {
        if (exp == null) return ""
        return if (!exp.id.isNullOrBlank() && !exp.name.isNullOrBlank()) "${exp.id}|${exp.name}" else (exp.name ?: "")
    }

    fun parseEmployerString(s: String?): Employer {
        if (s.isNullOrBlank()) return Employer(id = "", name = "", logoUrl = "")
        val parts = s.split("|", limit = 3)
        return when {
            parts.size == 3 -> Employer(id = parts[0], name = parts[1], logoUrl = parts[2])
            parts.size == 2 -> Employer(id = parts[0], name = parts[1], logoUrl = "")
            else -> Employer(id = "", name = s, logoUrl = "")
        }
    }

    fun employerToString(emp: Employer): String {
        return if (emp.id.isNotBlank()) "${emp.id}|${emp.name}|${emp.logoUrl}" else emp.name
    }

    fun parseIndustryString(s: String?): Industry {
        if (s.isNullOrBlank()) return Industry(id = 0, name = "")
        val parts = s.split("|", limit = 2)
        return if (parts.size == 2) {
            val id = parts[0].toIntOrNull() ?: 0
            Industry(id = id, name = parts[1])
        } else {
            Industry(id = 0, name = s)
        }
    }

    fun industryToString(ind: Industry): String {
        return if (ind.id != 0) "${ind.id}|${ind.name}" else ind.name
    }

}
