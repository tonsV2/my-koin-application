package dk.fitfit.mykoinapplication.rest.service

import dk.fitfit.fitlog.dto.ExerciseRequest
import dk.fitfit.fitlog.dto.ExerciseResponse
import retrofit2.http.*

interface ExerciseService {
    @GET("/exercises")
    suspend fun getExercises(@Query("updatedTimestamp") updatedTimestamp: Long? = null): List<ExerciseResponse>

    @POST("/exercises")
    suspend fun save(@Body exercise: ExerciseRequest): ExerciseResponse

    @PUT("/exercises/{id}")
    suspend fun update(@Path("id") id: Long, @Body exerciseRequest: ExerciseRequest): ExerciseResponse
}
