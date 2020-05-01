package dk.fitfit.mykoinapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dk.fitfit.mykoinapplication.db.model.Workout
import dk.fitfit.mykoinapplication.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    val workouts: LiveData<List<Workout>> = liveData {
        emitSource(repository.getWorkouts())
    }

    fun update() {
        viewModelScope.launch(IO) {
            repository.update()
        }
    }
}
