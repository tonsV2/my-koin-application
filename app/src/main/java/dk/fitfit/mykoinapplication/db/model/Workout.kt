package dk.fitfit.mykoinapplication.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    val name: String,
    val description: String,
    override val updated: Long?,
    @PrimaryKey(autoGenerate = false) override val id: Long
) : UpdatableEntity(updated, id)
