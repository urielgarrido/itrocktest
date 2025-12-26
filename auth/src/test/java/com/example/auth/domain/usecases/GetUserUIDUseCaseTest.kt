package com.example.auth.domain.usecases

import app.cash.turbine.test
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.usecase.GetUserUIDUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserUIDUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var getUserUIDUseCase: GetUserUIDUseCase

    @Before
    fun setUp() {
        getUserUIDUseCase = GetUserUIDUseCase(mockAuthRepository)
    }

    @Test
    fun `when repository returns a valid UID, use case should emit the same UID`() = runTest {
        // Given: Configuramos el mock para que devuelva un Flow con un UID.
        val expectedUID = "a1b2c3d4e5"
        every { mockAuthRepository.userUID } returns flowOf(expectedUID)

        // When: Invocamos el caso de uso.
        val resultFlow = getUserUIDUseCase()

        // Then: Verificamos que el flujo emite el UID esperado.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(expectedUID)
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns null UID, use case should emit null`() = runTest {
        // Given: Configuramos el mock para que devuelva un Flow con un valor nulo.
        every { mockAuthRepository.userUID } returns flowOf(null)

        // When: Invocamos el caso de uso.
        val resultFlow = getUserUIDUseCase()

        // Then: Verificamos que el flujo resultante emite nulo.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isNull()
            awaitComplete()
        }
    }
}
