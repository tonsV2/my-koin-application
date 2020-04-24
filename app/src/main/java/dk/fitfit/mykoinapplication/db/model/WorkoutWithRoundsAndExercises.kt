package dk.fitfit.mykoinapplication.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class RoundExerciseWithExercise(
        @Embedded val roundExercise: RoundExercise,
        @Relation(parentColumn = "exerciseId", entityColumn = "id")
        val exercise: Exercise
)

data class RoundWithRoundExercises(
        @Embedded val round: Round,
        @Relation(
                entity = RoundExercise::class,
                parentColumn = "id",
                entityColumn = "roundId"
        )
        val exercises: List<RoundExerciseWithExercise>
)

data class WorkoutWithRoundsAndExercises(
        @Embedded val workout: Workout,
        @Relation(
                entity = Round::class,
                parentColumn = "id",
                entityColumn = "workoutId"
        )
        val rounds: List<RoundWithRoundExercises>
)
