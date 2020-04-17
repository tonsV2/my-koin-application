package dk.fitfit.mykoinapplication.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.dto.ExerciseRequest
import dk.fitfit.mykoinapplication.rest.service.ExerciseService

class ExerciseViewModel(application: Application, private val repository: ExerciseRepository, private val exerciseService: ExerciseService) : AndroidViewModel(application) {
    suspend fun upsert(exercise: Exercise) {
        val exerciseRequest = ExerciseRequest(exercise.name, exercise.description, exercise.id)
        if (exerciseRequest.id == 0L) {
            exerciseService.save(exerciseRequest)
        } else {
            exerciseService.update(exerciseRequest.id, exerciseRequest)
        }
    }

    fun delete(exercise: Exercise) {
        repository.delete(exercise)
    }

    fun findAll() = repository.findAll()
}
