package com.example.auth.ui.utils

sealed class LoginProvider {
    data object Email: LoginProvider()
    data class Google(val idToken: String): LoginProvider()
}