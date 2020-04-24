package dk.fitfit.mykoinapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import dk.fitfit.fitlog.dto.RoundExerciseResponse
import dk.fitfit.fitlog.dto.RoundResponse
import dk.fitfit.fitlog.dto.WorkoutResponse
import dk.fitfit.mykoinapplication.db.dao.RoundDao
import dk.fitfit.mykoinapplication.db.dao.RoundExerciseDao
import dk.fitfit.mykoinapplication.db.dao.WorkoutDao
import dk.fitfit.mykoinapplication.db.model.Round
import dk.fitfit.mykoinapplication.db.model.RoundExercise
import dk.fitfit.mykoinapplication.db.model.Workout
import dk.fitfit.mykoinapplication.db.model.toEpochMilli
import dk.fitfit.mykoinapplication.rest.service.WorkoutService

class WorkoutRepository(
        private val workoutService: WorkoutService,
        private val workoutDao: WorkoutDao,
        private val roundDao: RoundDao,
        private val roundExerciseDao: RoundExerciseDao,
        private val exerciseRepository: ExerciseRepository) {
    suspend fun update() {
        val lastUpdate = workoutDao.getLastUpdate()
        // TODO: Try catch... network error
        val workoutResponses = workoutService.getWorkouts(lastUpdate)

        Log.d("Worker", "lastUpdate: $lastUpdate")
        Log.d("Worker", "Retrieved from server: ${workoutResponses.size}")

        // Update exercises here? We need exercises to satisfy FK relations
        exerciseRepository.update()

        // TODO: An update to rounds or round exercises wont trigger it to be redownloaded this way
        // They need to be individually synchronized and in the following order (to satisfy FK's)

        // Sync exercise
        // Sync workout
        // Sync round
        // Sync round exercise

        // TODO: This doesn't scale...

        workoutResponses.map { workoutResponse ->
            val workout = workoutResponse.toWorkout()
            workoutDao.insert(workout)
            workoutResponse.rounds?.map { roundResponse ->
                val round = roundResponse.toRound(workout)
                roundDao.insert(round)
                roundResponse.exercises?.map {
                    val roundExercise = it.toRoundExercise(round)
                    roundExerciseDao.insert(roundExercise)
                }
            }
        }
    }

    fun getWorkouts(): LiveData<List<Workout>> = workoutDao.findAll()

    fun getWorkoutWithRoundsAndExercises(id: Long) = workoutDao.getWorkoutWithRoundsAndExercises(id)
}

private fun WorkoutResponse.toWorkout() = Workout(name, description, updated?.toEpochMilli(), id)
private fun RoundResponse.toRound(workout: Workout) = Round(workout.id, priority, repetitions, rest, id, updated?.toEpochMilli())
private fun RoundExerciseResponse.toRoundExercise(round: Round) = RoundExercise(round.id, exercise.id, repetitions, maxTime, priority, updated?.toEpochMilli())
