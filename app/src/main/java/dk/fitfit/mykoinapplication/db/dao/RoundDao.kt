package dk.fitfit.mykoinapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dk.fitfit.mykoinapplication.db.model.Round

@Dao
interface RoundDao : UpdatableDao<Round> {
    @Query("select * from Round order by updated desc")
    override fun findAll(): LiveData<List<Round>>

    @Query("select max(updated) from Round")
    override fun getLastUpdate(): Long
}
