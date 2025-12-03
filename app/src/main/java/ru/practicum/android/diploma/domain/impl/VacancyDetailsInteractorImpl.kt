package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyOutcome

class VacancyDetailsInteractorImpl(private val vacancyRepository: VacancyRepository) : VacancyDetailsInteractor {
    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome> {
        return vacancyRepository.getVacancyById(vacancyId)
    }
}
