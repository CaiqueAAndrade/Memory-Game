package com.homermemorygame.di

import com.homermemorygame.repository.MemoryGameRepository
import com.homermemorygame.ui.viewmodel.MemoryGameViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { MemoryGameRepository(androidContext()) }
}

val viewModelModule = module {
    viewModel { MemoryGameViewModel(androidApplication(), get()) }
}
