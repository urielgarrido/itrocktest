package com.example.auth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.auth.ui.composables.ErrorBanner
import com.example.auth.ui.errors.SessionError
import com.example.auth.ui.events.SessionUIEvents
import com.example.auth.ui.states.SessionState

@Composable
fun SessionScreen(
    modifier: Modifier = Modifier,
    sessionState: SessionState,
    sessionUIEvents: SessionUIEvents?,
    onGetUserEmail: () -> Unit,
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

    LaunchedEffect(Unit) {
        if (sessionState.userEmail == null) {
            onGetUserEmail()
        }
    }

    when {
        sessionState.error != null -> {
            val errorMessage = when (sessionState.error) {
                SessionError.OnGetEmailError -> stringResource(R.string.get_email_error)
                SessionError.OnLogoutError -> stringResource(R.string.logout_error)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    ErrorBanner(errorMessage = errorMessage)
                }
            )
        }

        sessionState.userEmail != null -> {
            Column(
                modifier = modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.titleMedium,
                        text = sessionState.userEmail
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Text(text = stringResource(R.string.logout_button))
                        }
                    )
                }
            )
        }
    }
}