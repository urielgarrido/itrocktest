package com.example.auth.ui.states

import com.example.auth.ui.errors.LoginError

data class LoginState(
    val countries: List<String> = listOf<String>("Arg, Chi, Per"),
    val selectedCountry: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val onError: LoginError? = null
)
