package dk.fitfit.mykoinapplication

import com.google.gson.*
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.ExerciseRepositoryImpl
import dk.fitfit.mykoinapplication.repository.HelloRepository
import dk.fitfit.mykoinapplication.repository.HelloRepositoryImpl
import dk.fitfit.mykoinapplication.rest.*
import dk.fitfit.mykoinapplication.ui.ExerciseService
import dk.fitfit.mykoinapplication.ui.ExerciseSynchronizer
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import dk.fitfit.mykoinapplication.view.MyViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@JvmField
val appModule = module {
    single<HelloRepository> { HelloRepositoryImpl() }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    viewModel { MyViewModel(get()) }
    viewModel { ExerciseViewModel(get(), get()) }
}

@JvmField
val syncModule = module {
    single { ExerciseSynchronizer(get(), get(), get()) }
}

@JvmField
val restModule = module {
    single<AccessTokenProvider> { AccessTokenProviderDefault(get()) }
    factory { AccessTokenAuthenticator(get()) }
    factory { AccessTokenInterceptor(get()) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideGson(get(), get()) }
    single { provideRetrofit(get(), get()) }
    single { provideExerciseService(get()) }
    factory { provideLocalDateTimeDeserializer() }
    factory { provideLocalDateTimeSerializer() }
}

// Inspiration: https://blog.coinbase.com/okhttp-oauth-token-refreshes-b598f55dd3b2
// And: https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-4ff45a4792fc
fun provideExerciseService(retrofit: Retrofit): ExerciseService =
    retrofit.create(ExerciseService::class.java)

fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
    .baseUrl(BACKEND_BASE_URL)
    .client(httpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

fun provideOkHttpClient(
    accessTokenInterceptor: AccessTokenInterceptor,
    accessTokenAuthenticator: AccessTokenAuthenticator
): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(accessTokenInterceptor)
    .authenticator(accessTokenAuthenticator)
    .build()

fun provideLocalDateTimeDeserializer() = JsonDeserializer { json, _, _ ->
    // Inspiration: https://www.reddit.com/r/Kotlin/comments/f84989/any_suggestion_about_how_to_make_my_code_a_bit/
    json.asJsonArray.map { it.asInt }.let {
        LocalDateTime.of(it[0], it[1], it[2], it[3], it[4], it[5], if (it.size < 7) 0 else it[6])
    }
}

fun provideLocalDateTimeSerializer() = JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
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

fun provideGson(
    localDateTimeSerializer: JsonSerializer<LocalDateTime>,
    localDateTimeDeserializer: JsonDeserializer<LocalDateTime>
): Gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
    .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
    .create()
