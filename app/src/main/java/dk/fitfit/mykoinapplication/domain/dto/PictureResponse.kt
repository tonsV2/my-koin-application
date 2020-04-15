package dk.fitfit.mykoinapplication.domain.dto

data class PictureResponse(
        val url: String,
        val creator: UserResponse,
        val id: Long
)
