package dk.fitfit.mykoinapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dk.fitfit.mykoinapplication.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.db.model.Exercise
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application, private val repository: ExerciseRepository) : AndroidViewModel(application) {
    fun upsert(exercise: Exercise) {
        viewModelScope.launch(IO) {
            repository.upsert(exercise)
        }
    }

    fun update() {
        viewModelScope.launch(IO) {
            repository.update()
        }
    }

    fun findAll() = repository.findAll()
}
