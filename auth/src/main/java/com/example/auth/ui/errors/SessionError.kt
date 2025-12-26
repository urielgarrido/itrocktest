package com.example.auth.ui.errors

sealed class SessionError {
    object OnGetEmailError : SessionError()
    object OnLogoutError : SessionError()
}