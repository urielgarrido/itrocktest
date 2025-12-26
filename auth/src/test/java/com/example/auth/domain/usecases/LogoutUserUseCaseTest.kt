package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.LogoutUserUseCase
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

class LogoutUserUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var logoutUserUseCase: LogoutUserUseCase

    @Before
    fun setUp() {
        logoutUserUseCase = LogoutUserUseCase(mockAuthRepository)
    }

    @Test
    fun `when use case is invoked, it should call logout on the repository`() = runTest {
        // Given: Simulamos una respuesta exitosa para que el flujo no se rompa.
        coEvery { mockAuthRepository.logout() } returns flowOf(Result.success(Unit))

        // When: Invocamos el caso de uso.
        logoutUserUseCase()

        // Then: Verificamos que el método 'logout' del repositorio fue llamado exactamente una vez.
        coVerify(exactly = 1) { mockAuthRepository.logout() }
    }

    @Test
    fun `when repository returns success, use case should emit success`() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado exitoso.
        coEvery { mockAuthRepository.logout() } returns flowOf(Result.success(Unit))

        // When: Invocamos el caso de uso para obtener el flujo.
        val resultFlow = logoutUserUseCase()

        // Then: Verificamos que el flujo emite un resultado de éxito.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns failure, use case should emit failure`() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado de fallo.
        val exception = Exception("Logout failed")
        coEvery { mockAuthRepository.logout() } returns flowOf(Result.failure(exception))

        // When: Invocamos el caso de uso.
        val resultFlow = logoutUserUseCase()

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
        // Given: El flujo del repositorio lanza una excepción.
        val exception = RuntimeException("Network error")
        coEvery { mockAuthRepository.logout() } returns flow { throw exception }

        // When: Invocamos el caso de uso.
        val resultFlow = logoutUserUseCase()

        // Then: Verificamos que la excepción es capturada por el colector del flujo.
        resultFlow.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(RuntimeException::class.java)
            assertThat(error).hasMessageThat().isEqualTo("Network error")
        }
    }
}