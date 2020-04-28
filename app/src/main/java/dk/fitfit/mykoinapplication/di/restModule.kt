package dk.fitfit.mykoinapplication.di

import com.google.gson.*
import dk.fitfit.mykoinapplication.BACKEND_BASE_URL
import dk.fitfit.mykoinapplication.rest.AccessTokenAuthenticator
import dk.fitfit.mykoinapplication.rest.AccessTokenInterceptor
import dk.fitfit.mykoinapplication.rest.AccessTokenProvider
import dk.fitfit.mykoinapplication.rest.AccessTokenStorage
import dk.fitfit.mykoinapplication.rest.service.ExerciseService
import dk.fitfit.mykoinapplication.rest.service.LoginService
import dk.fitfit.mykoinapplication.rest.service.WorkoutService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

@JvmField
val restModule = module {
    single { AccessTokenProvider(get()) }
    single { AccessTokenStorage(get()) }
    factory { AccessTokenAuthenticator(get()) }
    factory { AccessTokenInterceptor(get()) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideLocalDateTimeSerializer() }
    factory { provideLocalDateTimeDeserializer() }
    factory { provideGson(get(), get()) }
    single { provideRetrofit(get(), get()) }
    single { get<Retrofit>().create(LoginService::class.java) }
    single { get<Retrofit>().create(ExerciseService::class.java) }
    single { get<Retrofit>().create(WorkoutService::class.java) }
}

private fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BACKEND_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

private fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        accessTokenAuthenticator: AccessTokenAuthenticator
): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(accessTokenInterceptor)
        .authenticator(accessTokenAuthenticator)
        .build()

private fun provideLocalDateTimeDeserializer() = JsonDeserializer { json, _, _ ->
    // Inspiration: https://www.reddit.com/r/Kotlin/comments/f84989/any_suggestion_about_how_to_make_my_code_a_bit/
    json.asJsonArray.map { it.asInt }.let {
        LocalDateTime.of(it[0], it[1], it[2], it[3], it[4], it[5], if (it.size < 7) 0 else it[6])
    }
}

private fun provideLocalDateTimeSerializer() = JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
    val jsonArray = JsonArray()
    jsonArray.add(localDateTime.year)
    jsonArray.add(localDateTime.monthValue)
    jsonArray.add(localDateTime.dayOfMonth)
    jsonArray.add(localDateTime.hour)
    jsonArray.add(localDateTime.minute)
    jsonArray.add(localDateTime.second)
    jsonArray.add(localDateTime.nano)
    jsonArray
}

private fun provideGson(
        localDateTimeSerializer: JsonSerializer<LocalDateTime>,
        localDateTimeDeserializer: JsonDeserializer<LocalDateTime>
): Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
        .create()
