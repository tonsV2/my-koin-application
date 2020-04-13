package dk.fitfit.mykoinapplication.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.domain.Exercise
import dk.fitfit.mykoinapplication.domain.ExerciseRepository

class ExerciseViewModel(application: Application, private val repository: ExerciseRepository) : AndroidViewModel(application) {
    fun insert(exercise: Exercise) {
        repository.insert(exercise)
    }

    fun update(exercise: Exercise) {
        repository.update(exercise)
    }

    fun delete(exercise: Exercise) {
        repository.delete(exercise)
    }

    fun findAll() = repository.findAll()
}
