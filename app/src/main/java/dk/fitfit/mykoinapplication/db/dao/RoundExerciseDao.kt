package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dk.fitfit.mykoinapplication.db.model.RoundExercise

@Dao
interface RoundExerciseDao : UpdatableDao<RoundExercise> {
    @Query("select * from RoundExercise order by updated desc")
    override fun findAll(): LiveData<List<RoundExercise>>

    @Query("select max(updated) from RoundExercise")
    override fun getLastUpdate(): Long
}
