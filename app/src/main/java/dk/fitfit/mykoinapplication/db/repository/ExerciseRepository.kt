package dk.fitfit.mykoinapplication.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.db.dao.ExerciseDao
import dk.fitfit.mykoinapplication.domain.Exercise

class ExerciseRepository(private val exerciseDao: ExerciseDao) {
    fun insert(exercise: Exercise) {
        Log.d("DAO", exercise.toString())
        exerciseDao.insert(exercise)
    }

    fun insert(exercises: List<Exercise>) {
        exercises.forEach {
            Log.d("DAO", it.toString())
        }
        exerciseDao.insert(exercises)
    }

    fun update(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    fun deleteAll() {
        exerciseDao.deleteAll()
    }

    fun findAll(): LiveData<List<Exercise>> = exerciseDao.findAll()

    fun getLastUpdate(): Long = exerciseDao.getLastUpdate()
}
