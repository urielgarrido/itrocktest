package com.example.testitrock.di

import com.example.auth.data.repository.LoginRepositoryImpl
import com.example.auth.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository = LoginRepositoryImpl()

}