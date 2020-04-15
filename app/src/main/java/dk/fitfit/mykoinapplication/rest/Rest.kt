package dk.fitfit.mykoinapplication.rest

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import dk.fitfit.mykoinapplication.MainApplication.Companion.TOKEN_STORE
import okhttp3.OkHttpClient
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Inspiration: https://blog.coinbase.com/okhttp-oauth-token-refreshes-b598f55dd3b2

object Rest {
    private const val BASE_URL = "http://192.168.1.103:8080"

    fun client(context: Context): Retrofit {
        val accessTokenProvider = object : AccessTokenProvider {
            override fun token(): String? {
                val settings = context.getSharedPreferences(TOKEN_STORE, Context.MODE_PRIVATE)
                return settings.getString("accessToken", null)
            }

            override fun refreshToken(): String? {
                return null
            }
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(AccessTokenInterceptor(accessTokenProvider))
            .authenticator(AccessTokenAuthenticator(accessTokenProvider))
            .build()

        // Inspiration: https://www.reddit.com/r/Kotlin/comments/f84989/any_suggestion_about_how_to_make_my_code_a_bit/
        val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
            json.asJsonArray.map { it.asInt }.let {
                LocalDateTime.of(it[0], it[1], it[2], it[3], it[4], it[5], if (it.size < 7) 0 else it[6])
            }
        }

        val localDateTimeSerializer = JsonSerializer<LocalDateTime> { localDateTime, _, _ ->
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

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
