package com.example.auth.domain.exceptions

sealed class RegisterExceptions: Exception() {
    object RegisterUserFailed: RegisterExceptions()
}