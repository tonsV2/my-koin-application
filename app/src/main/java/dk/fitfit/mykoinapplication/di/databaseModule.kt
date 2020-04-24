package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.db.FitLogDatabase
import org.koin.dsl.module

@JvmField
val databaseModule = module {
    single { FitLogDatabase.getDatabase(get()) }
    single { get<FitLogDatabase>().exerciseDao() }
    single { get<FitLogDatabase>().workoutDao() }
    single { get<FitLogDatabase>().roundDao() }
    single { get<FitLogDatabase>().roundExerciseDao() }
}
