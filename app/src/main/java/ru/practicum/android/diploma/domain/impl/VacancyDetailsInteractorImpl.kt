package ru.practicum.android.diploma.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.FavVacanciesInteractor
import ru.practicum.android.diploma.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.DomainError
import ru.practicum.android.diploma.domain.models.VacancyOutcome
import java.io.IOException

class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
    private val favVacanciesInteractor: FavVacanciesInteractor
) : VacancyDetailsInteractor {
    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyOutcome> = flow {
        val remoteResult: VacancyOutcome? = try {
            vacancyRepository.getVacancyById(vacancyId).firstOrNull()
        } catch (e: IOException) {
            Log.e("VacancyDetailsInteractor", "Error: ${e.message}")
            null
        }

        when (remoteResult) {
            is VacancyOutcome.Success -> emit(remoteResult)
            else -> {
                val local = favVacanciesInteractor.getFavVacancies()
                    .firstOrNull()
                    ?.firstOrNull { it.id == vacancyId }

                if (local != null) {
                    emit(VacancyOutcome.Success(local))
                } else {
                    emit(VacancyOutcome.Error(DomainError.NotFound))
                }
            }
        }
    }
}
