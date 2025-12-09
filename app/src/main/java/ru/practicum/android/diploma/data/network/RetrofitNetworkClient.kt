package ru.practicum.android.diploma.data.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.util.InternetConnectionStatus
import ru.practicum.android.diploma.util.ResponseCodes
import java.io.IOException

class RetrofitNetworkClient(
    private val vacancyApi: VacancyApi,
    private val industryApi: IndustryApi,
    private val context: Context
) : NetworkClient {

    companion object {
        private const val TAG = "RetrofitNetworkClient"
    }

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply {
                result = ResponseCodes.NO_CONNECTION
            }
        }

        try {
            when (dto) {
                is SearchRequest -> {
                    val filters = createFilters(dto)
                    val resultResponse = vacancyApi.getVacancies(
                        filters = filters,
                        text = dto.text ?: "",
                        page = dto.page
                    )

                    return resultResponse.apply {
                        result = ResponseCodes.SUCCESS
                    }
                }

                is VacancyRequest -> {
                    val resultResponse = vacancyApi.getVacancyById(vacancyId = dto.vacancyId)
                    return resultResponse.apply {
                        result = ResponseCodes.SUCCESS
                    }
                }

                is IndustriesRequest -> {
                    val resultResponse = industryApi.getFilterIndustries()
                    return resultResponse.apply {
                        result = ResponseCodes.SUCCESS
                    }
                }

                else -> {
                    return Response().apply {
                        result = ResponseCodes.ERROR_SERVER
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}", e)
            return Response().apply {
                result = ResponseCodes.ERROR_SERVER
            }
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.message}", e)
            return Response().apply {
                result = ResponseCodes.ERROR_SERVER
            }
        }
    }

    private fun isConnected(): Boolean {
        return InternetConnectionStatus.isInternetAvailable(context)
    }

    private fun createFilters(dto: SearchRequest): HashMap<String, String> {
        return HashMap<String, String>().apply {
            dto.industry?.let { put("industry", it.toString()) }
            dto.salary?.let { put("salary", it.toString()) }
            put("only_with_salary", dto.onlyWithSalary.toString())
        }
    }
}
