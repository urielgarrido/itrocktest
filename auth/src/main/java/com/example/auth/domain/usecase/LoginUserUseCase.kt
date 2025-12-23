package com.example.auth.domain.usecase

import com.example.auth.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoginUserUseCase @Inject constructor(
    private val loginRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Result<Unit>> {
        return loginRepository.login(email, password)
    }
}