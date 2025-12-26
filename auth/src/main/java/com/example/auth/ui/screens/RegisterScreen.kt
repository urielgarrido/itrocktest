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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.auth.ui.events.RegisterUIEvents
import com.example.auth.ui.states.RegisterState

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerState: RegisterState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    toLogin: () -> Unit,
    registerUIEvents: RegisterUIEvents?,
    onResetRegisterUIEvents: () -> Unit,
    onGoToNextScreen: () -> Unit
) {
    var showRegisterSuccessDialog by rememberSaveable { mutableStateOf(false) }

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
                        email = registerState.email,
                        onEmailChange = onEmailChange,
                        password = registerState.password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = registerState.passwordVisible,
                        onPasswordVisibleChange = onPasswordVisibleChange,
                        confirmPassword = registerState.confirmPassword,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        confirmPasswordVisible = registerState.confirmPasswordVisible,
                        onConfirmPasswordVisibleChange = onConfirmPasswordVisibleChange,
                        error = registerState.error
                    )
                    RegisterButton(
                        onRegister = onRegister,
                        enabled = registerState.registerButtonEnabled
                    )
                    ToLoginTextButton(
                        toLogin = toLogin
                    )
                }
            )
        }
    )
}