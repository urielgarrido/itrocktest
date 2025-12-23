package com.example.auth.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.exceptions.LoginExceptions
import com.example.auth.domain.usecase.LoginUserUseCase
import com.example.auth.ui.errors.LoginError
import com.example.auth.ui.events.LoginUIEvents
import com.example.auth.ui.states.LoginState
import com.example.auth.ui.utils.LoginProvider
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
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginUIEvents = MutableStateFlow<LoginUIEvents?>(null)
    val loginUIEvents = _loginUIEvents.asStateFlow()

    fun updateCountrySelected(country: String) {
        _loginState.update {
            it.copy(
                selectedCountry = country
            )
        }
    }

    fun updateEmail(email: String) {
        _loginState.update {
            it.copy(
                email = email.trim()
            )
        }
        checkFields()
    }

    fun updatePassword(password: String) {
        _loginState.update {
            it.copy(
                password = password.trim()
            )
        }
        checkFields()
    }

    fun updatePasswordVisible(passwordVisible: Boolean) {
        _loginState.update {
            it.copy(
                passwordVisible = passwordVisible
            )
        }
    }

    private fun checkFields() {
        val email = _loginState.value.email
        val password = _loginState.value.password

        val emailIsValid = Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
        val passwordIsValid = password.length > 8 && password.isNotEmpty()

        val error = when {
            !emailIsValid && email.isNotEmpty() -> LoginError.InvalidEmail
            !passwordIsValid && password.isNotEmpty() -> LoginError.InvalidPassword
            else -> null
        }

        val loginButtonEnabled = emailIsValid && passwordIsValid

        _loginState.update {
            it.copy(
                loginButtonEnabled = loginButtonEnabled,
                error = error
            )
        }
    }

    fun login(loginProvider: LoginProvider) {
        when (loginProvider) {
            LoginProvider.EMAIL -> {
                val email = _loginState.value.email
                val password = _loginState.value.password
                viewModelScope.launch {
                    loginUserUseCase(email, password).onEach { result ->
                        if (result.isSuccess) {
                            onLoginSuccess()
                        } else {
                            onLoginError(LoginError.InvalidCredentials)
                        }
                    }.catch { throwable ->
                        when (throwable) {
                            is LoginExceptions.InvalidCredentials -> {
                                onLoginError(LoginError.InvalidCredentials)
                            }
                            else -> {
                                onLoginError(LoginError.InvalidCredentials)
                            }
                        }
                    }.collect()
                }
            }
            LoginProvider.GOOGLE -> {

            }
        }
    }

    private fun onLoginError(error: LoginError) {
        _loginState.update {
            it.copy(
                error = error
            )
        }
    }

    private fun onLoginSuccess() {
        _loginUIEvents.value = LoginUIEvents.OnLoginSuccess
    }

    fun resetLoginUIEvents() {
        _loginUIEvents.value = null
    }
}