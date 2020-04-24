package dk.fitfit.mykoinapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dk.fitfit.mykoinapplication.db.dao.ExerciseDao
import dk.fitfit.mykoinapplication.db.dao.RoundDao
import dk.fitfit.mykoinapplication.db.dao.RoundExerciseDao
import dk.fitfit.mykoinapplication.db.dao.WorkoutDao
import dk.fitfit.mykoinapplication.db.model.Exercise
import dk.fitfit.mykoinapplication.db.model.Round
import dk.fitfit.mykoinapplication.db.model.RoundExercise
import dk.fitfit.mykoinapplication.db.model.Workout

@Database(entities = [Exercise::class, Workout::class, Round::class, RoundExercise::class], version = 1)
abstract class FitLogDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun roundDao(): RoundDao
    abstract fun roundExerciseDao(): RoundExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: FitLogDatabase? = null

        fun getDatabase(context: Context): FitLogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context, FitLogDatabase::class.java, "fit_log_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
