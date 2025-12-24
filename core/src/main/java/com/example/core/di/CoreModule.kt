package com.example.core.di

import com.example.core.data.local.UserPreferencesImpl
import com.example.core.domain.dataSource.UserPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        impl: UserPreferencesImpl
    ): UserPreferences
}