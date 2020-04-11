package dk.fitfit.mykoinapplication.domain

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
class Exercise(
    val name: String,
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
interface ExerciseDao {
    @Insert
    fun insert(exercise: Exercise)

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Query("select * from exercise")
    fun findAll(): LiveData<List<Exercise>>
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
    fun update(exercise: Exercise)
    fun delete(exercise: Exercise)
    fun findAll(): LiveData<List<Exercise>>
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
        exerciseDao.insert(exercise)
    }

    override fun update(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    override fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    override fun findAll(): LiveData<List<Exercise>> = exerciseDao.findAll()
}
