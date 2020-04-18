package dk.fitfit.mykoinapplication.db.repository.impl

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.mykoinapplication.db.FitLogDatabase
import dk.fitfit.mykoinapplication.db.dao.ExerciseDao
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.domain.Exercise

class ExerciseRepositoryImpl(application: Application) : ExerciseRepository {
    private val exerciseDao: ExerciseDao
    private val exercises: LiveData<List<Exercise>>

    init {
        val database = FitLogDatabase.getDatabase(application)
        exerciseDao = database.exerciseDao()
        exercises = exerciseDao.findAll()
    }

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
