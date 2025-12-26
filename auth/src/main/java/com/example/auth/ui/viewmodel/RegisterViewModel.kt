package com.example.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.usecase.RegisterUserUseCase
import com.example.auth.domain.validation.EmailValidator
import com.example.auth.ui.errors.RegisterError
import com.example.auth.ui.events.RegisterUIEvents
import com.example.auth.ui.states.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val emailValidator: EmailValidator
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _registerUIEvents = MutableStateFlow<RegisterUIEvents?>(null)
    val registerUIEvents = _registerUIEvents.asStateFlow()

    fun updateEmail(email: String) {
        _registerState.update {
            it.copy(email = email.trim())
        }
        checkFields()
    }

    fun updatePassword(password: String) {
        _registerState.update {
            it.copy(password = password.trim())
        }
        checkFields()
    }

    fun updatePasswordVisible(passwordVisible: Boolean) {
        _registerState.update {
            it.copy(passwordVisible = passwordVisible)
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _registerState.update {
            it.copy(confirmPassword = confirmPassword.trim())
        }
        checkFields()
    }

    fun updateConfirmPasswordVisible(confirmPasswordVisible: Boolean) {
        _registerState.update {
            it.copy(confirmPasswordVisible = confirmPasswordVisible)
        }
    }

    private fun checkFields() {
        val email = _registerState.value.email
        val password = _registerState.value.password
        val confirmPassword = _registerState.value.confirmPassword

        val emailIsValid = emailValidator.isValid(email)
        val passwordIsValid = password.length > 8 && password.isNotEmpty()
        val confirmPasswordIsValid = confirmPassword.length > 8 && confirmPassword.isNotEmpty() && password == confirmPassword


        val error = when {
            !emailIsValid && email.isNotEmpty() -> RegisterError.InvalidEmail
            !passwordIsValid && password.isNotEmpty() -> RegisterError.InvalidPassword
            !confirmPasswordIsValid && confirmPassword.isNotEmpty() -> RegisterError.InvalidConfirmPassword
            else -> null
        }

        val registerButtonEnabled = emailIsValid && passwordIsValid && confirmPasswordIsValid

        _registerState.update {
            it.copy(
                registerButtonEnabled = registerButtonEnabled,
                error = error
            )
        }
    }

    fun register() {
        val email = _registerState.value.email
        val password = _registerState.value.password

        viewModelScope.launch {
            registerUserUseCase(email, password).onEach { result ->
                if (result.isSuccess) {
                    onRegisterSuccess()
                } else {
                    onRegisterError(RegisterError.RegistrationFailed)
                }
            }.catch { throwable ->
                when(throwable) {
                    else -> onRegisterError(RegisterError.RegistrationFailed)
                }
            }.collect()
        }

    }

    private fun onRegisterError(error: RegisterError) {
        _registerState.update {
            it.copy(
                error = error
            )
        }
    }

    private fun onRegisterSuccess() {
        _registerUIEvents.value = RegisterUIEvents.OnRegisterSuccess
    }

    fun resetRegisterUIEvents() {
        _registerUIEvents.value = null
    }

}