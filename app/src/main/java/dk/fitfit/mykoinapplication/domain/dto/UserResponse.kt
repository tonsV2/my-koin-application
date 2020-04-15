package dk.fitfit.mykoinapplication.domain.dto

import org.threeten.bp.LocalDateTime

class UserResponse(
        val created: LocalDateTime,
        val id: Long
)
