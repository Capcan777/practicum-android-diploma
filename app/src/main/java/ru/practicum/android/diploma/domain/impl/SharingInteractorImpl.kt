package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.SharingInteractor
import ru.practicum.android.diploma.domain.api.ExternalNavigator
import ru.practicum.android.diploma.domain.models.Vacancy

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareVacancy(vacancy: Vacancy) {
        externalNavigator.shareVacancy(vacancy)
    }
}
