package ru.practicum.android.diploma.di

import androidx.room.Room
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.VacancyApi

private const val DATABASE_NAME = "vacancy_db.db"
private const val URL_VACANCY = "https://practicum-diploma-8bc38133faba.herokuapp.com/"

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }

    single<VacancyApi> {
        Retrofit.Builder()
            .baseUrl(URL_VACANCY)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(VacancyApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

}

val tokenInterceptor = Interceptor { chain: Interceptor.Chain ->
    val original: Request = chain.request()

    val token = BuildConfig.API_ACCESS_TOKEN

    val request: Request = original.newBuilder()
        .header("Authorization", "Bearer $token")
        .method(original.method, original.body)
        .build()

    chain.proceed(request)
}
