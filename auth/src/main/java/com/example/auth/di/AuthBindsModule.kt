package com.example.auth.di

import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.data.repository.GoogleAuthClientImpl
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.repository.GoogleAuthClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindsModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGoogleAuthClient(
        googleAuthClientImpl: GoogleAuthClientImpl
    ): GoogleAuthClient
}