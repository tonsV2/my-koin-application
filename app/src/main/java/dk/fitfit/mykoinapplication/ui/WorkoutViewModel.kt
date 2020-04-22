package dk.fitfit.mykoinapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.fitfit.mykoinapplication.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    fun findAll() = repository.findAll()

    fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update()
        }
    }
}
