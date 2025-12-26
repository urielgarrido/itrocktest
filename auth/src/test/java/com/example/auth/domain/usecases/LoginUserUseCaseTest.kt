package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.LoginUserUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginUserUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(mockAuthRepository)
    }

    @Test
    fun `when use case is invoked, it should call login on the repository with correct parameters`() = runTest {
        // Given: Credenciales de prueba y una respuesta simulada de éxito.
        val email = "test@example.com"
        val password = "password123"
        coEvery { mockAuthRepository.login(email, password) } returns flowOf(Result.success(Unit))

        // When: Se invoca el caso de uso.
        loginUserUseCase(email, password)

        // Then: Verificamos que el método del repositorio fue llamado exactamente una vez con los parámetros correctos.
        coVerify(exactly = 1) { mockAuthRepository.login(email, password) }
    }

    @Test
    fun `when repository returns success, use case should emit success`() = runTest {
        // Given: Credenciales y una respuesta simulada de éxito del repositorio.
        val email = "test@example.com"
        val password = "password123"
        coEvery { mockAuthRepository.login(email, password) } returns flowOf(Result.success(Unit))

        // When: Se invoca el caso de uso y se obtiene el flujo resultante.
        val resultFlow = loginUserUseCase(email, password)

        // Then: Verificamos que el flujo emite un resultado de éxito.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns failure, use case should emit failure`() = runTest {
        // Given: Credenciales y una respuesta simulada de fallo del repositorio.
        val email = "test@example.com"
        val password = "wrongpassword"
        val exception = Exception("Invalid credentials")
        coEvery { mockAuthRepository.login(email, password) } returns flowOf(Result.failure(exception))

        // When: Se invoca el caso de uso.
        val resultFlow = loginUserUseCase(email, password)

        // Then: Verificamos que el flujo emite el mismo resultado de fallo.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }

    @Test
    fun `when repository flow throws an exception, use case should propagate the exception`() = runTest {
        // Given: El flujo del repositorio lanza una excepción en lugar de emitir un resultado.
        val email = "test@example.com"
        val password = "password123"
        val exception = RuntimeException("Network error")
        coEvery { mockAuthRepository.login(email, password) } returns flow { throw exception }

        // When: Se invoca el caso de uso.
        val resultFlow = loginUserUseCase(email, password)

        // Then: Verificamos que la excepción es capturada por el colector del flujo.
        resultFlow.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(RuntimeException::class.java)
            assertThat(error).hasMessageThat().isEqualTo("Network error")
        }
    }
}