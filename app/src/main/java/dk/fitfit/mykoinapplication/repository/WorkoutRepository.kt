package dk.fitfit.mykoinapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.db.dao.WorkoutDao
import dk.fitfit.mykoinapplication.db.model.Workout
import dk.fitfit.mykoinapplication.db.model.toEpochMilli
import dk.fitfit.mykoinapplication.rest.service.WorkoutService

class WorkoutRepository(private val workoutService: WorkoutService, private val workoutDao: WorkoutDao) {
    suspend fun update() {
        val lastUpdate = workoutDao.getLastUpdate()
        // TODO: Try catch... network error
        val workoutResponses = workoutService.getWorkouts(lastUpdate)

        Log.d("Worker", "lastUpdate: $lastUpdate")
        Log.d("Worker", "Retrieved from server: ${workoutResponses.size}")

        val workouts = workoutResponses.map {
            Workout(it.name, it.description, it.updated?.toEpochMilli(), it.id)
        }

        workoutDao.insert(workouts)
    }

    fun findAll(): LiveData<List<Workout>> = workoutDao.findAll()
}
