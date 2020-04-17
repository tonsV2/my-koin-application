package dk.fitfit.mykoinapplication.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.dto.ExerciseRequest
import dk.fitfit.mykoinapplication.synchronize.ExerciseService

class ExerciseViewModel(application: Application, private val repository: ExerciseRepository, private val exerciseService: ExerciseService) : AndroidViewModel(application) {
    suspend fun insert(exercise: Exercise) {
//        repository.insert(exercise)
        exerciseService.save(ExerciseRequest(exercise.name, exercise.description))
    }

    fun update(exercise: Exercise) {
        repository.update(exercise)
    }

    fun delete(exercise: Exercise) {
        repository.delete(exercise)
    }

    fun findAll() = repository.findAll()
}
