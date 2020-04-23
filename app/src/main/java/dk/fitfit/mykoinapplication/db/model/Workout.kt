package dk.fitfit.mykoinapplication.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
data class Workout(
    val name: String,
    val description: String,
    override val updated: Long?,
    @PrimaryKey(autoGenerate = false) val id: Long
) : UpdatableEntity

@Entity(foreignKeys = [
    ForeignKey(entity = Workout::class, parentColumns = ["id"], childColumns = ["workoutId"], onDelete = CASCADE)
])
data class Round(
    val workoutId: Long,
    val priority: Int,
    val repetitions: Int,
    val rest: Int,
    @PrimaryKey(autoGenerate = false) val id: Long,
    override val updated: Long?
) : UpdatableEntity

@Entity(primaryKeys = ["roundId", "exerciseId"],
    foreignKeys = [
        ForeignKey(entity = Round::class, parentColumns = ["id"], childColumns = ["roundId"], onDelete = CASCADE),
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = CASCADE)
])
data class RoundExercise(
    val roundId: Long,
    val exerciseId: Long,
    val repetitions: Int,
    val maxTime: Int,
    val priority: Int,
    override val updated: Long?
) : UpdatableEntity
