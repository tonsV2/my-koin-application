package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.fitfit.mykoinapplication.domain.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercises: List<Exercise>)

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Query("delete from exercise")
    fun deleteAll()

    @Query("select * from exercise order by updated desc")
    fun findAll(): LiveData<List<Exercise>>

    @Query("select max(updated) from exercise")
    fun getLastUpdate(): Long
}
