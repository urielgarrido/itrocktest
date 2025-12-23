package com.example.auth.ui.events

sealed interface RegisterUIEvents {
    object OnRegisterSuccess: RegisterUIEvents
}