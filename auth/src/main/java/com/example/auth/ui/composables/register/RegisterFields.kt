package com.example.auth.ui.composables.register

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.auth.ui.errors.RegisterError

@Composable
fun RegisterFields(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    error: RegisterError?
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text(stringResource(R.string.email_label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        isError = error is RegisterError.InvalidEmail,
        supportingText = {
            if (error is RegisterError.InvalidEmail) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.invalid_email_error)
                )
            }
        }
    )

    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(stringResource(R.string.password_label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                Icon(
                    painter = if (passwordVisible) painterResource(id = com.example.core.R.drawable.visibility)
                    else painterResource(id = com.example.core.R.drawable.visibility_off),
                    contentDescription = null
                )
            }
        },
        isError = error is RegisterError.InvalidPassword,
        supportingText = {
            if (error is RegisterError.InvalidPassword) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.invalid_password_error)
                )
            }
        }
    )

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { onConfirmPasswordChange(it) },
        label = { Text(stringResource(R.string.confirm_password_label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) }) {
                Icon(
                    painter = if (confirmPasswordVisible) painterResource(id = com.example.core.R.drawable.visibility)
                    else painterResource(id = com.example.core.R.drawable.visibility_off),
                    contentDescription = null
                )
            }
        },
        isError = error is RegisterError.InvalidConfirmPassword,
        supportingText = {
            if (error is RegisterError.InvalidConfirmPassword) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.invalid_confirm_password_error)
                )
            }
        }
    )
}