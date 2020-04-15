package dk.fitfit.mykoinapplication.domain.dto

data class VideoResponse(
        val url: String,
        val creator: UserResponse,
        val id: Long
)
