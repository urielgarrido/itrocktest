package com.example.auth.ui.composables.login

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.auth.ui.errors.LoginError

@Composable
fun LoginFields(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisiblePasswordChange: (Boolean) -> Unit,
    error: LoginError?
) {

    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text(stringResource(R.string.email_label)) },
        placeholder = { Text(stringResource(R.string.email_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        isError = error is LoginError.InvalidEmail,
        supportingText = {
            if (error is LoginError.InvalidEmail) {
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
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                ImageVector.vectorResource(com.example.core.R.drawable.visibility)
            else ImageVector.vectorResource(com.example.core.R.drawable.visibility_off)

            val contentDescription = if (passwordVisible)
                stringResource(R.string.hide_password_content_description)
            else stringResource(R.string.show_password_content_description)


            IconButton(onClick = { onVisiblePasswordChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = contentDescription)
            }
        },
        isError = error is LoginError.InvalidPassword || error is LoginError.InvalidCredentials,
        supportingText = {
            if (error is LoginError.InvalidPassword) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.invalid_password_error)
                )
            }
            if (error is LoginError.InvalidCredentials) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.invalid_credentials_error)
                )
            }
        }
    )
}