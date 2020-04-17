package dk.fitfit.mykoinapplication.domain.dto

data class ExerciseRequest(
        val name: String,
        val description: String,
        val id: Long = 0
)
