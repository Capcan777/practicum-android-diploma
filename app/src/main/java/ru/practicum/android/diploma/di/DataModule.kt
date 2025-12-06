package ru.practicum.android.diploma.di

import androidx.room.Room
import coil.ImageLoader
import coil.decode.SvgDecoder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.IndustryApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.UserAgentInterceptor
import ru.practicum.android.diploma.data.network.VacancyApi

private const val DATABASE_NAME = "vacancy_db.db"
private const val URL_VACANCY = "https://practicum-diploma-8bc38133faba.herokuapp.com/" // Исправлено

private val tokenInterceptor = Interceptor { chain ->
    val original = chain.request()
    val token = BuildConfig.API_ACCESS_TOKEN
    val request = if (token.isNotEmpty()) { // Проверка токена
        original.newBuilder()
            .header("Authorization", "Bearer $token")
            .method(original.method, original.body)
            .build()
    } else {
        original // Если токен пустой — используем оригинальный запрос
    }
    chain.proceed(request) // Исправлено: proceed
}

val dataModule = module {

    // --- База данных ---
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    // --- Сетевая логика для Retrofit ---
    single {
        OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }

    single<ImageLoader> {
        ImageLoader.Builder(androidContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor(UserAgentInterceptor(androidContext()))
                    .build()
            }
            .crossfade(true)
            .build()
    }

    // --- Retrofit API ---
    single<VacancyApi> {
        Retrofit.Builder()
            .baseUrl(URL_VACANCY)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(VacancyApi::class.java)
    }

    single<IndustryApi> {
        Retrofit.Builder()
            .baseUrl(URL_VACANCY)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(IndustryApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(),get(), androidContext()) // Уберите androidContext(), если не нужен
    }

    single { get<AppDatabase>().vacancyDao() }
}
