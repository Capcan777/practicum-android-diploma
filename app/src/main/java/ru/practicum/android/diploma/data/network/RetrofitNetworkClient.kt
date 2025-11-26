package ru.practicum.android.diploma.data.network

import android.content.Context
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.util.InternetConnectionStatus

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
        // доработать запросы
        return Response().apply { // временно установил
            result = 0
        }

    }

    private fun isConnected(): Boolean {
        return InternetConnectionStatus.isInternetAvailable(context)
    }
}
