package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.repository.ExerciseRepository
import dk.fitfit.mykoinapplication.repository.WorkoutRepository
import org.koin.dsl.module

@JvmField
val repositoryModule = module {
    single { ExerciseRepository(get(), get()) }
    single { WorkoutRepository(get(), get(), get(), get(), get()) }
}
