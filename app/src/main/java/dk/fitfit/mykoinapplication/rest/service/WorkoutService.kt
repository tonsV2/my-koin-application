package dk.fitfit.mykoinapplication.rest.service

import dk.fitfit.fitlog.dto.WorkoutResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkoutService {
    @GET("/workouts")
    suspend fun getWorkouts(@Query("updatedTimestamp") updatedTimestamp: Long? = null): List<WorkoutResponse>
}
