package dk.fitfit.mykoinapplication.di

import android.content.Context
import androidx.room.Room
import dk.fitfit.mykoinapplication.db.FitLogDatabase
import org.koin.dsl.module

@JvmField
val databaseModule = module {
    single { provideDatabase(get()) }
    single { get<FitLogDatabase>().exerciseDao() }
    single { get<FitLogDatabase>().workoutDao() }
    single { get<FitLogDatabase>().roundDao() }
    single { get<FitLogDatabase>().roundExerciseDao() }
}

fun provideDatabase(context: Context) = Room
        .databaseBuilder(context, FitLogDatabase::class.java, "fit_log_database")
        .fallbackToDestructiveMigration()
        .build()
