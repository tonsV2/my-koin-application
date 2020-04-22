package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dk.fitfit.mykoinapplication.db.model.Exercise

@Dao
interface ExerciseDao : UpdatableDao<Exercise> {
    @Query("select * from Exercise order by updated desc")
    override fun findAll(): LiveData<List<Exercise>>

    @Query("select max(updated) from Exercise")
    override fun getLastUpdate(): Long
}
