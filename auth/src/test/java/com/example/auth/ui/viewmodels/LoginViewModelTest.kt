package com.example.auth.ui.viewmodels

import app.cash.turbine.test
import com.example.auth.domain.exceptions.LoginExceptions
import com.example.auth.domain.repository.GoogleAuthClient
import com.example.auth.domain.usecase.GoogleLoginUseCase
import com.example.auth.domain.usecase.LoginUserUseCase
import com.example.auth.domain.usecase.SelectCountryUseCase
import com.example.auth.domain.validation.EmailValidator
import com.example.auth.rules.MainDispatcherRule
import com.example.auth.ui.errors.LoginError
import com.example.auth.ui.events.LoginUIEvents
import com.example.auth.ui.utils.LoginProvider
import com.example.auth.ui.viewmodel.LoginViewModel
import com.example.core.domain.models.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
class LoginViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    // Regla para manejar las corrutinas en los tests
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var loginUserUseCase: LoginUserUseCase

    @RelaxedMockK
    private lateinit var googleLoginUseCase: GoogleLoginUseCase

    @RelaxedMockK
    private lateinit var selectCountryUseCase: SelectCountryUseCase

    @RelaxedMockK
    private lateinit var googleAuthClient: GoogleAuthClient

    @RelaxedMockK
    private lateinit var emailValidator: EmailValidator

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // Inyectamos los mocks al ViewModel antes de cada test
        viewModel = LoginViewModel(
            loginUserUseCase,
            googleLoginUseCase,
            selectCountryUseCase,
            googleAuthClient,
            emailValidator
        )
    }


    @Test
    fun `updateCountrySelected should update state and call use case`() = runTest {
        val newCountry = Country.B

        // When
        viewModel.updateCountrySelected(newCountry)

        // Then
        assertThat(viewModel.loginState.value.selectedCountry).isEqualTo(newCountry)
        coVerify { selectCountryUseCase(newCountry) }
    }

    @Test
    fun `updateEmail with invalid email should show error and disable button`() = runTest {
        val invalidEmail = "invalid-email"
        // Given: Le decimos al validador mockeado que este email es inválido
        every { emailValidator.isValid(invalidEmail) } returns false

        // When
        viewModel.updateEmail(invalidEmail)
        val state = viewModel.loginState.value

        // Then
        assertThat(state.email).isEqualTo(invalidEmail)
        assertThat(state.error).isEqualTo(LoginError.InvalidEmail)
        assertThat(state.loginButtonEnabled).isFalse()
    }

    @Test
    fun `updatePassword with password too short should show error and disable button`() {
        // Given: Un email válido para aislar el error de la contraseña
        every { emailValidator.isValid(any()) } returns true
        viewModel.updateEmail("test@example.com")

        // When
        viewModel.updatePassword("1234") // Contraseña menor a 8 caracteres
        val state = viewModel.loginState.value

        // Then
        assertThat(state.password).isEqualTo("1234")
        assertThat(state.error).isEqualTo(LoginError.InvalidPassword)
        assertThat(state.loginButtonEnabled).isFalse()
    }

    @Test
    fun `updateEmail and updatePassword with valid data should enable login button`() = runTest {
        // Given: Le decimos al validador que el email es válido
        val validEmail = "test@example.com"
        every { emailValidator.isValid(validEmail) } returns true

        // When
        viewModel.updateEmail(validEmail)
        viewModel.updatePassword("password123") // Contraseña válida

        // Then
        val state = viewModel.loginState.value
        assertThat(state.error).isNull()
        assertThat(state.loginButtonEnabled).isTrue()
    }

    @Test
    fun `login with Email success should emit OnLoginSuccess event`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        // Given: Simulamos que los casos de uso y validadores tienen éxito
        every { emailValidator.isValid(email) } returns true
        coEvery { loginUserUseCase(email, password) } returns flowOf(Result.success(Unit))

        viewModel.updateEmail(email)
        viewModel.updatePassword(password)

        viewModel.loginUIEvents.test {
            assertThat(awaitItem()).isNull() // Consumimos el estado inicial nulo del evento

            // When
            viewModel.login(LoginProvider.Email)

            // Then
            assertThat(awaitItem()).isEqualTo(LoginUIEvents.OnLoginSuccess)
        }
    }

    @Test
    fun `login with Email failure should update state with InvalidCredentials error`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        // Given: Simulamos un email válido pero un caso de uso que lanza una excepción
        every { emailValidator.isValid(email) } returns true
        coEvery { loginUserUseCase(email, password) } returns flow {
            throw LoginExceptions.InvalidCredentials
        }

        // When
        viewModel.updateEmail(email)
        viewModel.updatePassword(password)
        viewModel.login(LoginProvider.Email)

        // Then
        val state = viewModel.loginState.value
        assertThat(state.error).isEqualTo(LoginError.InvalidCredentials)
    }

    @Test
    fun `login with Google success should emit OnLoginSuccess event`() = runTest {
        val idToken = "test-id-token"
        // Given: Simulamos que el caso de uso de Google tiene éxito
        coEvery { googleLoginUseCase(idToken) } returns flowOf(Result.success(Unit))

        viewModel.loginUIEvents.test {
            assertThat(awaitItem()).isNull() // Consumimos el estado inicial nulo del evento

            // When
            viewModel.login(LoginProvider.Google(idToken))

            // Then
            assertThat(awaitItem()).isEqualTo(LoginUIEvents.OnLoginSuccess)
        }
    }

    @Test
    fun `login with Google failure should update state with UnknownError`() = runTest {
        val idToken = "invalid-token"
        // Given: Simulamos que el caso de uso de Google lanza un error inesperado
        coEvery { googleLoginUseCase(idToken) } returns flow {
            throw IllegalStateException("Some other error")
        }

        // When
        viewModel.login(LoginProvider.Google(idToken))

        // Then
        val state = viewModel.loginState.value
        assertThat(state.error).isEqualTo(LoginError.UnknownError)
    }

    @Test
    fun `resetLoginUIEvents should set event to null`() = runTest {
        // Given: Simulamos un login exitoso para que el evento se popule
        coEvery { loginUserUseCase(any(), any()) } returns flowOf(Result.success(Unit))
        every { emailValidator.isValid(any()) } returns true
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("password123")

        viewModel.loginUIEvents.test {
            assertThat(awaitItem()).isNull() // Estado inicial

            viewModel.login(LoginProvider.Email)
            assertThat(awaitItem()).isNotNull() // Se emitió el evento de éxito

            // When
            viewModel.resetLoginUIEvents()

            // Then
            assertThat(awaitItem()).isNull() // Se reseteó a nulo
        }
    }
}
