package com.example.auth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.R
import com.example.auth.ui.composables.register.RegisterButton
import com.example.auth.ui.composables.register.RegisterFields
import com.example.auth.ui.composables.register.RegisterSuccessDialog
import com.example.auth.ui.composables.register.ToLoginTextButton
import com.example.auth.ui.errors.RegisterError
import com.example.auth.ui.events.RegisterUIEvents

@Composable
fun RegisterScreen(
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
    onRegister: () -> Unit,
    isRegisterButtonEnabled: Boolean,
    toLogin: () -> Unit,
    error: RegisterError?,
    registerUIEvents: RegisterUIEvents?,
    onResetRegisterUIEvents: () -> Unit,
    onGoToNextScreen: () -> Unit
) {
    var showRegisterSuccessDialog by remember { mutableStateOf(false) }

    DisposableEffect(registerUIEvents) {
        when (registerUIEvents) {
            RegisterUIEvents.OnRegisterSuccess -> {
                showRegisterSuccessDialog = true
            }
            null -> Unit
        }
        onDispose {
            onResetRegisterUIEvents()
        }
    }

    if (showRegisterSuccessDialog) {
        RegisterSuccessDialog(
            onDismiss = {
                showRegisterSuccessDialog = false
                onGoToNextScreen()
            }
        )
    }

    Scaffold(
        modifier = modifier,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(
                        modifier = Modifier.padding(top = 50.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.register_header)
                    )
                    RegisterFields(
                        email = email,
                        onEmailChange = onEmailChange,
                        password = password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = passwordVisible,
                        onPasswordVisibleChange = onPasswordVisibleChange,
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        confirmPasswordVisible = confirmPasswordVisible,
                        onConfirmPasswordVisibleChange = onConfirmPasswordVisibleChange,
                        error = error
                    )
                    RegisterButton(
                        onRegister = onRegister,
                        enabled = isRegisterButtonEnabled
                    )
                    ToLoginTextButton(
                        toLogin = toLogin
                    )
                }
            )
        }
    )
}