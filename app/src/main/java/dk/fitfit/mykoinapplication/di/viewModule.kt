package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val viewModule = module {
    viewModel { ExerciseViewModel(get(), get(), get()) }
}
