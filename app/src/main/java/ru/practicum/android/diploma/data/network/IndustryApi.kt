package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import ru.practicum.android.diploma.data.dto.IndustriesResponse

interface IndustryApi {

    @GET("industries")
    suspend fun getFilterIndustries(
        @Header("Content-Type") contentType: String = "application/json"
    ): IndustriesResponse
}
