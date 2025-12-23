package com.example.auth.ui.errors

sealed class LoginError {
    object InvalidEmail : LoginError()
    object InvalidPassword : LoginError()
}