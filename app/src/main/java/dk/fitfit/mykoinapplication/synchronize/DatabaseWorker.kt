package dk.fitfit.mykoinapplication.synchronize

import android.content.Context
import android.util.Log
import com.google.gson.annotations.SerializedName
import dk.fitfit.mykoinapplication.MainApplication.Companion.TOKEN_STORE
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.dto.ExerciseRequest
import dk.fitfit.mykoinapplication.domain.dto.ExerciseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import retrofit2.HttpException
import retrofit2.http.*

abstract class DataSynchronizer {
    abstract suspend fun doWork()

    fun synchronize() {
        CoroutineScope(IO).launch {
            doWork()
        }
    }
}

class ExerciseSynchronizer(private val context: Context, private val exerciseRepository: ExerciseRepository, private val exerciseService: ExerciseService) : DataSynchronizer() {
    override suspend fun doWork() {
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
            Exercise(it.name, it.description, it.updated.toEpochMilli(), it.id)
        }

        exerciseRepository.insert(exercises)
    }

    private fun LocalDateTime.toEpochMilli() = this.toInstant(ZoneOffset.UTC).toEpochMilli()
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

    @POST("/exercises")
    suspend fun save(@Body exercise: ExerciseRequest): ExerciseResponse

    @PUT("/exercises/{id}")
    suspend fun update(@Path("id") id: Long, @Body exerciseRequest: ExerciseRequest)
}