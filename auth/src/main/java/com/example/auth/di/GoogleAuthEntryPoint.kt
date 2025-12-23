package com.example.auth.di

import com.example.auth.domain.repository.GoogleAuthClient
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GoogleAuthEntryPoint {
    fun googleAuthClient(): GoogleAuthClient
}
