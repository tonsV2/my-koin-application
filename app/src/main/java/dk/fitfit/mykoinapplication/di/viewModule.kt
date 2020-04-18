package dk.fitfit.mykoinapplication.di

import dk.fitfit.mykoinapplication.repository.HelloRepository
import dk.fitfit.mykoinapplication.repository.HelloRepositoryImpl
import dk.fitfit.mykoinapplication.view.ExerciseViewModel
import dk.fitfit.mykoinapplication.view.MyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val viewModule = module {
    single<HelloRepository> { HelloRepositoryImpl() }
    viewModel { MyViewModel(get()) }
    viewModel { ExerciseViewModel(get(), get(), get()) }
}
