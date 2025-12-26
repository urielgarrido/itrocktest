package com.example.auth.ui.composables.login

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.auth.R

@Composable
fun LoginErrorDialog(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.login_error_title))
        },
        text = {
            Text(text = stringResource(R.string.login_error_message))
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(com.example.core.R.string.accept_button))
            }
        }
    )
}