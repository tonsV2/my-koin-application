package dk.fitfit.mykoinapplication

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
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
            modules(appModule, restModule, syncModule)
        }
    }

    companion object {
        const val TOKEN_STORE = "token-store"
    }
}
