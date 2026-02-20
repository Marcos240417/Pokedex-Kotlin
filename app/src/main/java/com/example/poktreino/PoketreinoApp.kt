package com.example.poktreino

import android.app.Application
import com.example.poktreino.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PoketreinoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PoketreinoApp)
            modules(appModule)
        }
    }
}