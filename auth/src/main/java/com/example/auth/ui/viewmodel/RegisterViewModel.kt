package com.example.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.auth.ui.states.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun updateEmail(email: String) {
        _registerState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _registerState.update {
            it.copy(password = password)
        }
    }

    fun updatePasswordVisible(passwordVisible: Boolean) {
        _registerState.update {
            it.copy(passwordVisible = passwordVisible)
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _registerState.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }

    fun updateConfirmPasswordVisible(confirmPasswordVisible: Boolean) {
        _registerState.update {
            it.copy(confirmPasswordVisible = confirmPasswordVisible)
        }
    }

    fun register() {


    }

}