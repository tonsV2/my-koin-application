package dk.fitfit.mykoinapplication.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
data class Exercise(
    val name: String,
    val description: String,
    val updated: Long,
    @PrimaryKey(autoGenerate = false) val id: Long = 0
)

fun LocalDateTime.toEpochMilli() = this.toInstant(ZoneOffset.UTC).toEpochMilli()
