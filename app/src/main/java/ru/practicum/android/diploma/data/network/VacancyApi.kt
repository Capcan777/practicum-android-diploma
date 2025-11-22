package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VacancyApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String,
        @QueryMap(encoded = true) filters: Map<String, String>
    ): Any // Заменить Any на нужный тип ответа

    @GET("vacancies/{vacancyId}")
    suspend fun getVacancyById(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String,
        @Path("vacancyId") vacancyId: String
    ): Any // Заменить Any на нужный тип ответа

}
