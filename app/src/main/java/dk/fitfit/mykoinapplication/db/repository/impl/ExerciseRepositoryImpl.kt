package dk.fitfit.mykoinapplication.db.repository.impl

import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.db.dao.ExerciseDao
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.Exercise

class ExerciseRepositoryImpl(private val exerciseDao: ExerciseDao) : ExerciseRepository {

    override fun insert(exercise: Exercise) {
        Log.d("DAO", exercise.toString())
        exerciseDao.insert(exercise)
    }

    override fun insert(exercises: List<Exercise>) {
        exercises.forEach {
            Log.d("DAO", it.toString())
        }
        exerciseDao.insert(exercises)
    }

    override fun update(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    override fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    override fun deleteAll() {
        exerciseDao.deleteAll()
    }

    override fun findAll(): LiveData<List<Exercise>> = exerciseDao.findAll()

    override fun getLastUpdate(): Long = exerciseDao.getLastUpdate()
}
