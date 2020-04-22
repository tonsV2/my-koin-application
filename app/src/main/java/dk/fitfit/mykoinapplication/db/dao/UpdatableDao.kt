package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dk.fitfit.mykoinapplication.db.model.UpdatableEntity

interface UpdatableDao<T : UpdatableEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercises: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercise: T)

    fun findAll(): LiveData<List<T>>

    fun getLastUpdate(): Long
}
