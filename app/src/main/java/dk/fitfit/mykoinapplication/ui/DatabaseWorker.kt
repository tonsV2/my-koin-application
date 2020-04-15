package dk.fitfit.mykoinapplication.ui

import android.content.Context
import android.util.Log
import com.google.gson.annotations.SerializedName
import dk.fitfit.mykoinapplication.MainApplication.Companion.TOKEN_STORE
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.dto.ExerciseResponse
import dk.fitfit.mykoinapplication.rest.Rest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import retrofit2.HttpException
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

abstract class DataSynchronizer {
    abstract suspend fun doWork()

    fun synchronize() {
        CoroutineScope(IO).launch {
            doWork()
        }
    }
}

class ExerciseSynchronizer(val context: Context) : DataSynchronizer(), KoinComponent {
    override suspend fun doWork() {
        val exerciseRepository: ExerciseRepository by inject()

        val exerciseService = Rest.client(context).create(ExerciseService::class.java)
        val username = "some username"
        val password = "some password"
        try {
            val tokens = exerciseService.login(Credentials(username, password))

            val settings = context.getSharedPreferences(TOKEN_STORE, Context.MODE_PRIVATE)
            settings.edit()
                .putString("accessToken", tokens.accessToken)
                .putString("refreshToken", tokens.refreshToken)
                .apply()
        } catch (e: HttpException) {
            e.printStackTrace()
        }

        val lastUpdate = exerciseRepository.getLastUpdate()
        // TODO: Try catch... network error
        val exerciseResponses = exerciseService.getExercises(lastUpdate)
        Log.d("Worker", "lastUpdate: $lastUpdate")
        Log.d("Worker", "Retrieved from server: ${exerciseResponses.size}")

        val exercises = exerciseResponses.map {
            Exercise(it.name, it.description, epochMilli(it.updated), it.id)
        }

        exerciseRepository.insert(exercises)
    }

    private fun epochMilli(dateTime: LocalDateTime) = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}

class Credentials(val username: String, val password: String)

class OauthTokens(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("username") val username: String
)

interface ExerciseService {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): OauthTokens

    @GET("/exercises")
    suspend fun getExercises(@Query("updatedTimestamp") updatedTimestamp: Long? = null): List<ExerciseResponse>
}
