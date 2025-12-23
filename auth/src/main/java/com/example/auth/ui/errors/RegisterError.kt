package com.example.auth.ui.errors

sealed class RegisterError {
    object InvalidEmail : RegisterError()
    object InvalidPassword : RegisterError()
    object InvalidConfirmPassword : RegisterError()
    object RegistrationFailed : RegisterError()
}