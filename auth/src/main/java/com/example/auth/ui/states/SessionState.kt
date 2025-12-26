package com.example.auth.ui.states

import com.example.auth.ui.errors.SessionError

data class SessionState(
    val userEmail: String? = null,
    val error: SessionError? = null
)
