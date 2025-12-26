package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.GoogleLoginUseCase
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

class GoogleLoginUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var googleLoginUseCase: GoogleLoginUseCase

    @Before
    fun setUp() {
        googleLoginUseCase = GoogleLoginUseCase(mockAuthRepository)
    }

    @Test
    fun `when use case is invoked, it should call loginWithGoogle on the repository`() = runTest {
        // Given
        val idToken = "test-id-token"
        // Simulamos una respuesta exitosa para que el flujo no se rompa.
        coEvery { mockAuthRepository.loginWithGoogle(idToken) } returns flowOf(Result.success(Unit))

        // When
        googleLoginUseCase(idToken)

        // Then
        // Verificamos que el método del repositorio fue llamado exactamente una vez con el token correcto.
        coVerify(exactly = 1) { mockAuthRepository.loginWithGoogle(idToken) }
    }

    @Test
    fun `when repository returns success, use case should emit success`() = runTest {
        // Given
        val idToken = "test-id-token"
        // Configuramos el mock para que devuelva un flujo con un resultado exitoso.
        coEvery { mockAuthRepository.loginWithGoogle(idToken) } returns flowOf(Result.success(Unit))

        // When
        val resultFlow = googleLoginUseCase(idToken)

        // Then
        // Verificamos que el flujo emitido por el UseCase contiene el resultado de éxito.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns failure, use case should emit failure`() = runTest {
        // Given
        val idToken = "test-id-token"
        val exception = Exception("Authentication failed")
        // Configuramos el mock para que devuelva un flujo con un resultado de fallo.
        coEvery { mockAuthRepository.loginWithGoogle(idToken) } returns flowOf(Result.failure(exception))

        // When
        val resultFlow = googleLoginUseCase(idToken)

        // Then
        // Verificamos que el flujo emitido por el UseCase contiene el resultado de fallo.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }

    @Test
    fun `when repository throws an exception, use case should propagate the exception`() = runTest {
        // Given
        val idToken = "test-id-token"
        val exception = RuntimeException("Network error")
        // Configuramos el mock para que el propio flujo lance una excepción.
        coEvery { mockAuthRepository.loginWithGoogle(idToken) } returns flow { throw exception }

        // When
        val resultFlow = googleLoginUseCase(idToken)

        // Then
        // Verificamos que la excepción se propaga a través del flujo.
        resultFlow.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(RuntimeException::class.java)
            assertThat(error).isEqualTo(exception)
        }
    }
}
