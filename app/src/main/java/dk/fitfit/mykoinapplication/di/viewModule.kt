package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.ui.ExerciseViewModel
import dk.fitfit.mykoinapplication.ui.WorkoutDetailsViewModel
import dk.fitfit.mykoinapplication.ui.WorkoutSessionViewModel
import dk.fitfit.mykoinapplication.ui.WorkoutViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val viewModule = module {
    viewModel { ExerciseViewModel(get()) }
    viewModel { WorkoutViewModel(get()) }
    viewModel { WorkoutDetailsViewModel(get()) }
    viewModel { WorkoutSessionViewModel() }
}
