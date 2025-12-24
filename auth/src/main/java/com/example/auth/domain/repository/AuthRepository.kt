package com.example.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Result<Unit>>
    suspend fun loginWithGoogle(idToken: String): Flow<Result<Unit>>
    suspend fun register(email: String, password: String): Flow<Result<Unit>>
    suspend fun logout(): Flow<Result<Unit>>
    val isUserLoggedIn: Flow<Boolean>
    val userUID: Flow<String?>
}