package com.example.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.auth.ui.states.LoginState
import com.example.auth.ui.utils.LoginProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

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
                email = email
            )
        }
    }

    fun updatePassword(password: String) {
        _loginState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updatePasswordVisible(passwordVisible: Boolean) {
        _loginState.update {
            it.copy(
                passwordVisible = passwordVisible
            )
        }
    }

    fun login(loginProvider: LoginProvider) {

    }

    fun register() {

    }

}