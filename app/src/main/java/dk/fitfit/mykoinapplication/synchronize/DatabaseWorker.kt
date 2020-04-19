package dk.fitfit.mykoinapplication.synchronize

import android.util.Log
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.toEpochMilli
import dk.fitfit.mykoinapplication.rest.service.ExerciseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

abstract class DataSynchronizer {
    abstract suspend fun doWork()

    fun synchronize() {
        CoroutineScope(IO).launch {
            doWork()
        }
    }
}

class ExerciseSynchronizer(private val exerciseRepository: ExerciseRepository, private val exerciseService: ExerciseService) : DataSynchronizer() {
    override suspend fun doWork() {
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
}
