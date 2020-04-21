package dk.fitfit.mykoinapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {
    val exercises: LiveData<List<Exercise>> = repository.findAll()

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
}
