package com.example.auth.ui.states

import com.example.auth.ui.errors.LoginError

data class LoginState(
    val countries: List<String> = listOf("A", "B"),
    val selectedCountry: String = countries.first(),
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val loginButtonEnabled: Boolean = false,
    val error: LoginError? = null
)
