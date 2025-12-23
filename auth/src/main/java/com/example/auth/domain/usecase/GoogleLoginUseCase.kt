package com.example.auth.domain.usecase

import com.example.auth.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GoogleLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Flow<Result<Unit>> {
        return authRepository.loginWithGoogle(idToken)
    }
}