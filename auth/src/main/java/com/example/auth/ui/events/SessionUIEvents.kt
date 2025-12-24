package com.example.auth.ui.events

sealed interface SessionUIEvents {
    object OnLogout : SessionUIEvents
}