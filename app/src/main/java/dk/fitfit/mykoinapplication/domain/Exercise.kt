package dk.fitfit.mykoinapplication.domain

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Entity
data class Exercise(
    val name: String,
    val description: String,
    val updated: Long,
    @PrimaryKey(autoGenerate = false) val id: Long = 0
)

@Dao
interface ExerciseDao {
    @Insert(onConflict = REPLACE)
    fun insert(exercise: Exercise)

    @Insert(onConflict = REPLACE)
    fun insert(exercises: List<Exercise>)

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Query("DELETE FROM exercise")
    fun deleteAll()

    @Query("select * from exercise order by updated desc")
    fun findAll(): LiveData<List<Exercise>>

    @Query("select max(updated) from exercise")
    fun getLastUpdate(): Long
}

@Database(entities = [Exercise::class], version = 1)
abstract class FitLogDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: FitLogDatabase? = null

        fun getDatabase(context: Context): FitLogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitLogDatabase::class.java,
                    "fit_log_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

interface ExerciseRepository {
    fun insert(exercise: Exercise)
    fun insert(exercises: List<Exercise>)
    fun update(exercise: Exercise)
    fun delete(exercise: Exercise)
    fun deleteAll()
    fun findAll(): LiveData<List<Exercise>>
    fun getLastUpdate(): Long
}

class ExerciseRepositoryImpl(application: Application) : ExerciseRepository {
    private val exerciseDao: ExerciseDao
    private val exercises: LiveData<List<Exercise>>

    init {
        val database = FitLogDatabase.getDatabase(application)
        exerciseDao = database.exerciseDao()
        exercises = exerciseDao.findAll()
    }

    override fun insert(exercise: Exercise) {
        Log.d("DAO", exercise.toString())
        exerciseDao.insert(exercise)
    }

    override fun insert(exercises: List<Exercise>) {
        exercises.forEach {
            Log.d("DAO", it.toString())
        }
        exerciseDao.insert(exercises)
    }

    override fun update(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    override fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    override fun deleteAll() {
        exerciseDao.deleteAll()
    }

    override fun findAll(): LiveData<List<Exercise>> = exerciseDao.findAll()

    override fun getLastUpdate(): Long = exerciseDao.getLastUpdate()
}
