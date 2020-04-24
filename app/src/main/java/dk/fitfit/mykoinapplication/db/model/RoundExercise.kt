package dk.fitfit.mykoinapplication.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
        primaryKeys = ["roundId", "exerciseId"],
        indices = [Index("exerciseId"), Index("roundId")],
        foreignKeys = [
            ForeignKey(entity = Round::class, parentColumns = ["id"], childColumns = ["roundId"], onDelete = CASCADE),
            ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = CASCADE)
        ]
)
data class RoundExercise(
        val roundId: Long,
        val exerciseId: Long,
        val repetitions: Int,
        val maxTime: Int,
        val priority: Int,
        override val updated: Long?
) : UpdatableEntity
