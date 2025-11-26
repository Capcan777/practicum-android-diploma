package ru.practicum.android.diploma.data.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.SearchRequest
import ru.practicum.android.diploma.util.InternetConnectionStatus
import java.io.IOException

class RetrofitNetworkClient(
    private val api: VacancyApi,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply {
                result = -1
            }
        }

        return when (dto) {
            is SearchRequest -> {
                try {
                    val token = BuildConfig.API_ACCESS_TOKEN
                    val authorizationHeader = "Bearer $token"
                    val contentType = "application/json"

                    val filters = createFilters(dto)

                    val searchResponse = api.getVacancies(authorizationHeader, contentType, filters, dto.page)
                    searchResponse.apply {
                        result = SUCCESS
                    }
                } catch (e: IOException) {
                    Log.e("RetrofitNetworkClient", "Network error: ${e.message}", e)
                    Response().apply {
                        result = ERROR_SERVER
                    }
                } catch (e: HttpException) {
                    Log.e("RetrofitNetworkClient", "HTTP error: ${e.message}", e)
                    Response().apply {
                        result = ERROR_SERVER
                    }
                }
            }

            else -> {
                Response().apply {
                    result = ERROR_CLIENT
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        return InternetConnectionStatus.isInternetAvailable(context)
    }

    private fun createFilters(dto: SearchRequest): HashMap<String, String> {
        return HashMap<String, String>().apply {
            dto.industry?.let { put("industry", it.toString()) }
            dto.text?.let { put("text", it) }
            dto.salary?.let { put("salary", it.toString()) }
            put("page", dto.page.toString())
            put("only_with_salary", dto.onlyWithSalary.toString())
        }
    }

    companion object ResponseCodes {
        const val SUCCESS = 200
        const val ERROR_CLIENT = 400
        const val ERROR_SERVER = 500
    }
}
