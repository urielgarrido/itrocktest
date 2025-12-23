package com.example.auth.ui.composables.register

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.auth.R

@Composable
fun RegisterSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.register_account_successful_title))
        },
        text = {
            Text(text = stringResource(R.string.register_account_successful))
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text(stringResource(R.string.login_button))
            }
        }
    )
}