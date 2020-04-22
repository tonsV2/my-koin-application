package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dk.fitfit.mykoinapplication.db.model.Workout

@Dao
interface WorkoutDao : UpdatableDao<Workout> {
    @Query("select * from Workout order by updated desc")
    override fun findAll(): LiveData<List<Workout>>

    @Query("select max(updated) from Workout")
    override fun getLastUpdate(): Long
}
