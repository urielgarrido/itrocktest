package com.example.auth.ui.states

import com.example.auth.ui.errors.LoginError
import com.example.core.domain.models.Country

data class LoginState(
    val countries: List<Country> = listOf(Country.A, Country.B),
    val selectedCountry: Country = countries.first(),
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val loginButtonEnabled: Boolean = false,
    val error: LoginError? = null
)
