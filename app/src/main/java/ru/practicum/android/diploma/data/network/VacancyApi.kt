package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.SearchResponse

interface VacancyApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @Header("Content-Type") contentType: String = "application/json",
        @QueryMap(encoded = true) filters: Map<String, String>,
        @Query("text") text: String,
        @Query("page") page: Int
    ): SearchResponse

    @GET("vacancies/{vacancyId}")
    suspend fun getVacancyById(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String,
        @Path("vacancyId") vacancyId: String
    ): SearchResponse

}
