package dk.fitfit.mykoinapplication

import dk.fitfit.mykoinapplication.repository.HelloRepository
import dk.fitfit.mykoinapplication.repository.HelloRepositoryImpl
import dk.fitfit.mykoinapplication.view.MyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single<HelloRepository> { HelloRepositoryImpl() }
    viewModel { MyViewModel(get()) }
}
