package dk.fitfit.mykoinapplication.db.model

import androidx.room.Ignore

abstract class UpdatableEntity(
    @Ignore open val updated: Long?,
    @Ignore open val id: Long
)
