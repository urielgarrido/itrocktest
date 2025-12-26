package com.example.products.ui.composables.payment

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.products.R

@Composable
fun ValidationCardResultDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.validate_card_title))
        },
        text = {
            Text(text = stringResource(R.string.validate_card_error))
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text(stringResource(com.example.core.R.string.accept_button))
            }
        }
    )
}