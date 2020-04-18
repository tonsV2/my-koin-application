package dk.fitfit.mykoinapplication.db.repository

import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.domain.Exercise

interface ExerciseRepository {
    fun insert(exercise: Exercise)
    fun insert(exercises: List<Exercise>)
    fun update(exercise: Exercise)
    fun delete(exercise: Exercise)
    fun deleteAll()
    fun findAll(): LiveData<List<Exercise>>
    fun getLastUpdate(): Long
}
