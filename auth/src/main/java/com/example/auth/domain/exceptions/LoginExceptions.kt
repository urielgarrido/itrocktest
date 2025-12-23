package com.example.auth.domain.exceptions

sealed class LoginExceptions: Exception() {
    data object InvalidCredentials: LoginExceptions()
}