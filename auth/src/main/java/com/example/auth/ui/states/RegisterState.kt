package com.example.auth.ui.states

import com.example.auth.ui.errors.RegisterError

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val onError: RegisterError? = null
)
