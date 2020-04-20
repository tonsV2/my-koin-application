package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.repository.ExerciseRepository
import org.koin.dsl.module

@JvmField
val repositoryModule = module {
    single { ExerciseRepository(get(), get()) }
}
