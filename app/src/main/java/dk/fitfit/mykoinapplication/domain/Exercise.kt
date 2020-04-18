package dk.fitfit.mykoinapplication.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    val name: String,
    val description: String,
    val updated: Long,
    @PrimaryKey(autoGenerate = false) val id: Long = 0
)
