package com.example.auth.ui.viewmodels

import app.cash.turbine.test
import com.example.auth.domain.usecase.RegisterUserUseCase
import com.example.auth.domain.validation.EmailValidator
import com.example.auth.rules.MainDispatcherRule
import com.example.auth.ui.errors.RegisterError
import com.example.auth.ui.events.RegisterUIEvents
import com.example.auth.ui.viewmodel.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @RelaxedMockK
    private lateinit var emailValidator: EmailValidator

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(registerUserUseCase, emailValidator)
    }

    @Test
    fun `updateEmail should update state and trim input`() {
        val emailWithSpaces = "  test@example.com  "
        viewModel.updateEmail(emailWithSpaces)
        assertThat(viewModel.registerState.value.email).isEqualTo("test@example.com")
    }

    @Test
    fun `updatePassword should update state and trim input`() {
        val passwordWithSpaces = "  password123  "
        viewModel.updatePassword(passwordWithSpaces)
        assertThat(viewModel.registerState.value.password).isEqualTo("password123")
    }

    @Test
    fun `updateConfirmPassword should update state and trim input`() {
        val passwordWithSpaces = "  password123  "
        viewModel.updateConfirmPassword(passwordWithSpaces)
        assertThat(viewModel.registerState.value.confirmPassword).isEqualTo("password123")
    }

    @Test
    fun `updatePasswordVisible should update state`() {
        viewModel.updatePasswordVisible(true)
        assertThat(viewModel.registerState.value.passwordVisible).isTrue()
        viewModel.updatePasswordVisible(false)
        assertThat(viewModel.registerState.value.passwordVisible).isFalse()
    }

    @Test
    fun `updateConfirmPasswordVisible should update state`() {
        viewModel.updateConfirmPasswordVisible(true)
        assertThat(viewModel.registerState.value.confirmPasswordVisible).isTrue()
        viewModel.updateConfirmPasswordVisible(false)
        assertThat(viewModel.registerState.value.confirmPasswordVisible).isFalse()
    }

    @Test
    fun `when email is invalid, register button should be disabled and show error`() {
        // Given
        every { emailValidator.isValid("invalid") } returns false

        // When
        viewModel.updateEmail("invalid")

        // Then
        val state = viewModel.registerState.value
        assertThat(state.error).isEqualTo(RegisterError.InvalidEmail)
        assertThat(state.registerButtonEnabled).isFalse()
    }

    @Test
    fun `when password is too short, register button should be disabled and show error`() {
        // Given: Email es válido
        every { emailValidator.isValid(any()) } returns true
        viewModel.updateEmail("test@example.com")

        // When
        viewModel.updatePassword("1234") // Menos de 8 caracteres

        // Then
        val state = viewModel.registerState.value
        assertThat(state.error).isEqualTo(RegisterError.InvalidPassword)
        assertThat(state.registerButtonEnabled).isFalse()
    }

    @Test
    fun `when passwords do not match, register button should be disabled and show error`() {
        // Given: Email y contraseña son válidos
        every { emailValidator.isValid(any()) } returns true
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("ValidPassword123")

        // When
        viewModel.updateConfirmPassword("DifferentPassword123")

        // Then
        val state = viewModel.registerState.value
        assertThat(state.error).isEqualTo(RegisterError.InvalidConfirmPassword)
        assertThat(state.registerButtonEnabled).isFalse()
    }

    @Test
    fun `when all fields are valid, register button should be enabled and no error shown`() {
        // Given
        every { emailValidator.isValid("test@example.com") } returns true

        // When
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("ValidPassword123")
        viewModel.updateConfirmPassword("ValidPassword123")

        // Then
        val state = viewModel.registerState.value
        assertThat(state.error).isNull()
        assertThat(state.registerButtonEnabled).isTrue()
    }

    @Test
    fun `register success should emit OnRegisterSuccess event`() = runTest {
        // Given: Todos los campos válidos y el caso de uso devuelve éxito
        val email = "test@example.com"
        val password = "ValidPassword123"
        every { emailValidator.isValid(email) } returns true
        coEvery { registerUserUseCase(email, password) } returns flowOf(Result.success(Unit))

        viewModel.updateEmail(email)
        viewModel.updatePassword(password)
        viewModel.updateConfirmPassword(password)

        // When & Then
        viewModel.registerUIEvents.test {
            assertThat(awaitItem()).isNull() // Estado inicial del evento

            viewModel.register()

            assertThat(awaitItem()).isEqualTo(RegisterUIEvents.OnRegisterSuccess)
        }
    }

    @Test
    fun `register failure should update state with RegistrationFailed error`() = runTest {
        // Given: Todos los campos válidos pero el caso de uso devuelve fallo
        val email = "test@example.com"
        val password = "ValidPassword123"
        every { emailValidator.isValid(email) } returns true
        coEvery { registerUserUseCase(email, password) } returns flowOf(Result.failure(Exception()))

        viewModel.updateEmail(email)
        viewModel.updatePassword(password)
        viewModel.updateConfirmPassword(password)

        // When
        viewModel.register()

        // Then
        val state = viewModel.registerState.value
        assertThat(state.error).isEqualTo(RegisterError.RegistrationFailed)
    }

    @Test
    fun `resetRegisterUIEvents should set event to null`() = runTest {
        // Given: Simulamos un registro exitoso para que el evento se popule
        coEvery { registerUserUseCase(any(), any()) } returns flowOf(Result.success(Unit))
        every { emailValidator.isValid(any()) } returns true
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("ValidPassword123")
        viewModel.updateConfirmPassword("ValidPassword123")

        viewModel.registerUIEvents.test {
            assertThat(awaitItem()).isNull() // Estado inicial
            viewModel.register()
            assertThat(awaitItem()).isNotNull() // El evento de éxito se ha emitido

            // When
            viewModel.resetRegisterUIEvents()

            // Then
            assertThat(awaitItem()).isNull() // El evento se ha reseteado a nulo
        }
    }
}