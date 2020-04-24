package dk.fitfit.mykoinapplication.ui

import androidx.lifecycle.*
import dk.fitfit.mykoinapplication.db.model.Workout
import dk.fitfit.mykoinapplication.db.model.WorkoutWithRoundsAndExercises
import dk.fitfit.mykoinapplication.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    val workouts: LiveData<List<Workout>> = liveData {
        emitSource(repository.getWorkouts())
    }

    var workout: MutableLiveData<WorkoutWithRoundsAndExercises> = MutableLiveData()

    fun loadWorkout(id: Long) {
        viewModelScope.launch(IO) {
            workout.postValue(repository.getWorkoutWithRoundsAndExercises(id))
        }
    }

    fun update() {
        viewModelScope.launch(IO) {
            repository.update()
        }
    }
}
