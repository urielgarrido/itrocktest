package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.IsUserLoggedInUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IsUserLoggedInUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase

    @Before
    fun setUp() {
        isUserLoggedInUseCase = IsUserLoggedInUseCase(mockAuthRepository)
    }

    @Test
    fun `when repository emits true, use case should emit true`() = runTest {
        // Given: Configuramos el mock del repositorio para que devuelva un Flow con 'true'.
        every { mockAuthRepository.isUserLoggedIn } returns flowOf(true)

        // When: Invocamos el caso de uso para obtener el flujo.
        val resultFlow = isUserLoggedInUseCase()

        // Then: Verificamos que el flujo resultante emite 'true'.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when repository emits false, use case should emit false`() = runTest {
        // Given: Configuramos el mock del repositorio para que devuelva un Flow con 'false'.
        every { mockAuthRepository.isUserLoggedIn } returns flowOf(false)

        // When: Invocamos el caso de uso.
        val resultFlow = isUserLoggedInUseCase()

        // Then: Verificamos que el flujo resultante emite 'false'.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isFalse()
            awaitComplete()
        }
    }
}
