package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.db.FitLogDatabase
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import org.koin.dsl.module

@JvmField
val databaseModule = module {
    single { FitLogDatabase.getDatabase(get()) }
    single { get<FitLogDatabase>().exerciseDao() }
    single { ExerciseRepository(get()) }
}
