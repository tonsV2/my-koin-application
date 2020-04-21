package dk.fitfit.mykoinapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.fitlog.dto.ExerciseRequest
import dk.fitfit.mykoinapplication.db.dao.ExerciseDao
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.toEpochMilli
import dk.fitfit.mykoinapplication.rest.service.ExerciseService

class ExerciseRepository(private val exerciseService: ExerciseService, private val exerciseDao: ExerciseDao) {
    suspend fun upsert(exercise: Exercise) {
        val exerciseRequest = ExerciseRequest(exercise.name, exercise.description, exercise.id)
        val exerciseResponse = if (exerciseRequest.id == 0L) {
            exerciseService.save(exerciseRequest)
        } else {
            exerciseService.update(exerciseRequest.id, exerciseRequest)
        }
        with(exerciseResponse) {
            exerciseDao.insert(Exercise(name, description, updated?.toEpochMilli(), id))
        }
    }

    suspend fun update() {
        val lastUpdate = exerciseDao.getLastUpdate()
        // TODO: Try catch... network error
        val exerciseResponses = exerciseService.getExercises(lastUpdate)

        Log.d("Worker", "lastUpdate: $lastUpdate")
        Log.d("Worker", "Retrieved from server: ${exerciseResponses.size}")

        val exercises = exerciseResponses.map {
            Exercise(it.name, it.description, it.updated?.toEpochMilli(), it.id)
        }

        exerciseDao.insert(exercises)
    }

    fun findAll(): LiveData<List<Exercise>> = exerciseDao.findAll()
}
