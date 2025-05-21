package de.larvesta

import android.app.Application
import androidx.room.Room
import de.larvesta.data.AppDatabase
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import de.larvesta.di.dataModule
import de.larvesta.di.domainModule
import de.larvesta.di.presentationModule

class App : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        // Room database initialise
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "larvesta_database"
        ).build()

        // Start Koin Dependency Injection
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}