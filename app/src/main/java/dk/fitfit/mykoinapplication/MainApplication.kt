package dk.fitfit.mykoinapplication

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dk.fitfit.mykoinapplication.di.databaseModule
import dk.fitfit.mykoinapplication.di.restModule
import dk.fitfit.mykoinapplication.di.syncModule
import dk.fitfit.mykoinapplication.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(viewModule, syncModule, databaseModule, restModule)
        }
    }
}
