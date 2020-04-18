package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.synchronize.ExerciseSynchronizer
import org.koin.dsl.module

@JvmField
val syncModule = module {
    single { ExerciseSynchronizer(get(), get()) }
}
