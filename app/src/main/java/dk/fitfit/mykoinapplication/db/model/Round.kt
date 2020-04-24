package dk.fitfit.mykoinapplication.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        indices = [Index("workoutId")],
        foreignKeys = [
            ForeignKey(entity = Workout::class, parentColumns = ["id"], childColumns = ["workoutId"], onDelete = ForeignKey.CASCADE)
        ]
)
data class Round(
        val workoutId: Long,
        val priority: Int,
        val repetitions: Int,
        val rest: Int,
        @PrimaryKey(autoGenerate = false) val id: Long,
        override val updated: Long?
) : UpdatableEntity
