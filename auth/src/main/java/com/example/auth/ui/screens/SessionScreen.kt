package com.example.auth.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.example.auth.ui.events.SessionUIEvents

@Composable
fun SessionScreen(
    modifier: Modifier = Modifier,
    sessionUIEvents: SessionUIEvents?,
    onLogout: () -> Unit,
    onResetSessionUIEvents: () -> Unit,
    onGoToInitScreen: () -> Unit
) {
    DisposableEffect(sessionUIEvents) {
        when (sessionUIEvents) {
            SessionUIEvents.OnLogout -> {
                onGoToInitScreen()
            }
            null -> Unit
        }
        onDispose {
            onResetSessionUIEvents()
        }
    }
    Button(
        onClick = onLogout,
        modifier = modifier,
        content = {
            Text(text = "Cerrar Sesión")
        }
    )
}