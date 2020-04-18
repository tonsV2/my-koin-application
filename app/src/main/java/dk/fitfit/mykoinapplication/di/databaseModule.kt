package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.db.FitLogDatabase
import dk.fitfit.mykoinapplication.db.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.db.repository.impl.ExerciseRepositoryImpl
import org.koin.dsl.module

@JvmField
val databaseModule = module {
    single { FitLogDatabase.getDatabase(get()) }
    single { provideExerciseDao(get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
}

fun provideExerciseDao(database: FitLogDatabase) = database.exerciseDao()
