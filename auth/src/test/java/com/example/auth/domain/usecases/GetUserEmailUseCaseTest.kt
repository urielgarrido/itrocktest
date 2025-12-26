package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.GetUserEmailUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserEmailUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var getUserEmailUseCase: GetUserEmailUseCase

    @Before
    fun setUp() {
        getUserEmailUseCase = GetUserEmailUseCase(mockAuthRepository)
    }

    @Test
    fun `when repository returns a valid email, use case should emit the same email`() = runTest {
        // Given: Configuramos el mock del repositorio para que devuelva un Flow con un email.
        val expectedEmail = "test@example.com"
        every { mockAuthRepository.userEmail } returns flowOf(expectedEmail)

        // When: Invocamos el caso de uso.
        val resultFlow = getUserEmailUseCase()

        // Then: Verificamos que el flujo resultante emite el email esperado.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(expectedEmail)
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns null, use case should emit null`() = runTest {
        // Given: Configuramos el mock del repositorio para que devuelva un Flow con un valor nulo.
        every { mockAuthRepository.userEmail } returns flowOf(null)

        // When: Invocamos el caso de uso.
        val resultFlow = getUserEmailUseCase()

        // Then: Verificamos que el flujo resultante emite nulo.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isNull()
            awaitComplete()
        }
    }
}
