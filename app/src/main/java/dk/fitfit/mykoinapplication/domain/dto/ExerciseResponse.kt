package dk.fitfit.mykoinapplication.domain.dto

import org.threeten.bp.LocalDateTime

data class ExerciseResponse(
    val name: String,
    val description: String,
    val creator: UserResponse,
    val videos: List<VideoResponse>?,
    val pictures: List<PictureResponse>?,
    val id: Long,
    val created: LocalDateTime,
    val updated: LocalDateTime
)
