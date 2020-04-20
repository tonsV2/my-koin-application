package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dk.fitfit.mykoinapplication.db.model.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercises: List<Exercise>)

    @Query("select * from exercise order by updated desc")
    fun findAll(): LiveData<List<Exercise>>

    @Query("select max(updated) from exercise")
    fun getLastUpdate(): Long
}
