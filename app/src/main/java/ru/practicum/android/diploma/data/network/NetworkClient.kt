package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun getVacancyById(vacancyId: String): Response
}
