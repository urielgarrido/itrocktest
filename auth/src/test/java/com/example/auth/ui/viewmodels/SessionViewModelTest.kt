package com.example.auth.ui.viewmodels

import app.cash.turbine.test
import com.example.auth.domain.usecase.GetUserEmailUseCase
import com.example.auth.domain.usecase.LogoutUserUseCase
import com.example.auth.rules.MainDispatcherRule
import com.example.auth.ui.errors.SessionError
import com.example.auth.ui.events.SessionUIEvents
import com.example.auth.ui.viewmodel.SessionViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SessionViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var logoutUserUseCase: LogoutUserUseCase

    @RelaxedMockK
    private lateinit var getUserEmailUseCase: GetUserEmailUseCase

    private lateinit var viewModel: SessionViewModel

    @Before
    fun setUp() {
        viewModel = SessionViewModel(logoutUserUseCase, getUserEmailUseCase)
    }

    @Test
    fun `when getUserEmail is successful, state should be updated with the user email`() = runTest {
        // Given: El caso de uso devuelve un email con éxito.
        val testEmail = "test@example.com"
        coEvery { getUserEmailUseCase() } returns flowOf(testEmail)

        // When: Se llama a la función del ViewModel.
        viewModel.getUserEmail()

        // Then: El estado se actualiza con el email y sin errores.
        val state = viewModel.sessionState.value
        assertThat(state.userEmail).isEqualTo(testEmail)
        assertThat(state.error).isNull()
    }

    @Test
    fun `when getUserEmail fails, state should be updated with OnGetEmailError`() = runTest {
        // Given: El caso de uso lanza una excepción.
        val exception = RuntimeException("Database connection failed")
        coEvery { getUserEmailUseCase() } returns flow { throw exception }

        // When: Se llama a la función del ViewModel.
        viewModel.getUserEmail()

        // Then: El estado se actualiza con el error correspondiente.
        val state = viewModel.sessionState.value
        assertThat(state.error).isInstanceOf(SessionError.OnGetEmailError::class.java)
        assertThat(state.userEmail).isNull() // El email no debería haberse actualizado
    }

    @Test
    fun `when logout is successful, OnLogout event should be emitted`() = runTest {
        // Given: El caso de uso de logout devuelve éxito.
        coEvery { logoutUserUseCase() } returns flowOf(Result.success(Unit))

        // When & Then: Se consume el flujo de eventos y se verifica la emisión.
        viewModel.sessionUIEvents.test {
            assertThat(awaitItem()).isNull() // Estado inicial nulo.

            // Se dispara el logout.
            viewModel.logout()

            // Se verifica que se emite el evento de éxito.
            assertThat(awaitItem()).isEqualTo(SessionUIEvents.OnLogout)
        }
    }

    @Test
    fun `when logout use case returns failure, state should be updated with OnLogoutError`() = runTest {
        // Given: El caso de uso de logout devuelve un Result.failure.
        val exception = Exception("Logout process failed")
        coEvery { logoutUserUseCase() } returns flowOf(Result.failure(exception))

        // When
        viewModel.logout()

        // Then: El estado se actualiza con el error.
        val state = viewModel.sessionState.value
        assertThat(state.error).isInstanceOf(SessionError.OnLogoutError::class.java)
    }


    @Test
    fun `when logout use case throws exception, state should be updated with OnLogoutError`() = runTest {
        // Given: El caso de uso de logout lanza una excepción.
        val exception = RuntimeException("Network error")
        coEvery { logoutUserUseCase() } returns flow { throw exception }

        // When
        viewModel.logout()

        // Then: El estado se actualiza con el error.
        val state = viewModel.sessionState.value
        assertThat(state.error).isInstanceOf(SessionError.OnLogoutError::class.java)
    }

    @Test
    fun `resetSessionUIEvents should set event to null`() = runTest {
        // Given: Simulamos un logout exitoso para que el evento se popule.
        coEvery { logoutUserUseCase() } returns flowOf(Result.success(Unit))

        viewModel.sessionUIEvents.test {
            assertThat(awaitItem()).isNull() // Estado inicial.

            viewModel.logout() // Se emite el evento.

            assertThat(awaitItem()).isNotNull() // Se confirma que el evento fue emitido.

            // When: Se resetea el evento.
            viewModel.resetSessionUIEvents()

            // Then: El evento vuelve a ser nulo.
            assertThat(awaitItem()).isNull()
        }
    }
}