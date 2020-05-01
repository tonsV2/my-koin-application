package dk.fitfit.mykoinapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.fitfit.mykoinapplication.db.model.WorkoutWithRoundsAndExercises
import dk.fitfit.mykoinapplication.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    var workout: MutableLiveData<WorkoutWithRoundsAndExercises> = MutableLiveData()

    fun loadWorkout(id: Long) {
        viewModelScope.launch(IO) {
            workout.postValue(repository.getWorkoutWithRoundsAndExercises(id))
        }
    }
}
