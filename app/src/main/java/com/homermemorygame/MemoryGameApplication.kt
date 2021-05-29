package com.homermemorygame

import android.app.Application
import com.homermemorygame.di.repositoryModule
import com.homermemorygame.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MemoryGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MemoryGameApplication)
            modules(
                listOf(
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}