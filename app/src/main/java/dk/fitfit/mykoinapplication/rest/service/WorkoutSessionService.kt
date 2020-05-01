package dk.fitfit.mykoinapplication.rest.service

import dk.fitfit.fitlog.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface WorkoutSessionService {
//    @GET("/exercises")
//    suspend fun getExercises(@Query("updatedTimestamp") updatedTimestamp: Long? = null): List<ExerciseResponse>

    @POST("/sessions")
    suspend fun save(@Body session: SessionRequest): SessionResponse

    @POST("/session-rounds")
    suspend fun save(@Body sessionRound: SessionRoundRequest): SessionRoundResponse

    @POST("/session-exercises")
    suspend fun save(@Body sessionExercise: SessionExerciseRequest): SessionExerciseResponse
}
