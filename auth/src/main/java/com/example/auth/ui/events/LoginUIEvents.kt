package com.example.auth.ui.events

sealed interface LoginUIEvents {
    object OnLoginSuccess : LoginUIEvents
}