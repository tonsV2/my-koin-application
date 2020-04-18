package dk.fitfit.mykoinapplication.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.dto.ExerciseRequest
import dk.fitfit.mykoinapplication.rest.service.ExerciseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application, private val repository: ExerciseRepository, private val exerciseService: ExerciseService) : AndroidViewModel(application) {
    fun upsert(exercise: Exercise) {
        // TODO: Should this be in ExerciseRepository... Or somewhere else?
        CoroutineScope(IO).launch {
            val exerciseRequest = ExerciseRequest(exercise.name, exercise.description, exercise.id)
            if (exerciseRequest.id == 0L) {
                exerciseService.save(exerciseRequest)
            } else {
                exerciseService.update(exerciseRequest.id, exerciseRequest)
            }
        }
    }

    fun delete(exercise: Exercise) {
        repository.delete(exercise)
    }

    fun findAll() = repository.findAll()
}
