package de.larvesta.di

import android.app.Application
import de.larvesta.data.AppDatabase
import de.larvesta.data.dao.DayDao
import de.larvesta.data.dao.DishDao
import de.larvesta.data.dao.FoodDao
import de.larvesta.domain.repository.DishRepository
import de.larvesta.domain.repository.FoodRepository
import de.larvesta.domain.repository.DayRepository
import de.larvesta.presentation.viewmodel.DayViewModel
import de.larvesta.presentation.viewmodel.DishViewModel
import de.larvesta.presentation.viewmodel.FoodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { AppDatabase.getDatabase(get<Application>()) }
    single { get<AppDatabase>().dishDao() }
    single { get<AppDatabase>().foodDao() }
    single { get<AppDatabase>().dayDao() }
}

val domainModule = module {
    single { DishRepository(get<DishDao>()) }
    single { FoodRepository(get<FoodDao>()) }
    single { DayRepository(get<DayDao>()) }
}

val presentationModule = module {
    viewModel { DishViewModel(get()) }
    viewModel { FoodViewModel(get()) }
    viewModel { DayViewModel(get()) }
}

// Info: provide the database and DAO dependencies with SOLID-Principle.