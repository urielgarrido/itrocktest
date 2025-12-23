package com.example.auth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.R
import com.example.auth.domain.repository.GoogleAuthClient
import com.example.auth.ui.composables.login.CountrySelector
import com.example.auth.ui.composables.login.LoginButton
import com.example.auth.ui.composables.login.LoginFields
import com.example.auth.ui.composables.login.LoginGoogleButton
import com.example.auth.ui.composables.login.ToRegisterButton
import com.example.auth.ui.errors.LoginError
import com.example.auth.ui.events.LoginUIEvents
import com.example.auth.ui.utils.LoginProvider
import com.example.auth.ui.utils.rememberGoogleAuthClient

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    countries: List<String>,
    countrySelected: String,
    onCountrySelected: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    loginButtonEnabled: Boolean,
    onLogin: (LoginProvider) -> Unit,
    onRegister: () -> Unit,
    error: LoginError?,
    loginUIEvents: LoginUIEvents?,
    onResetLoginUIEvents: () -> Unit,
    onGoToNextScreen: () -> Unit
) {
    val googleAuthClient = rememberGoogleAuthClient()

    DisposableEffect(loginUIEvents) {
        when (loginUIEvents) {
            LoginUIEvents.OnLoginSuccess -> {
                onGoToNextScreen()
            }
            null -> Unit
        }
        onDispose {
            onResetLoginUIEvents()
        }
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
                        text = stringResource(R.string.login_header)
                    )
                    CountrySelector(
                        countries = countries,
                        countrySelected = countrySelected,
                        onCountrySelected = onCountrySelected
                    )
                    LoginFields(
                        email = email,
                        onEmailChange = onEmailChange,
                        password = password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = passwordVisible,
                        onVisiblePasswordChange = onPasswordVisibleChange,
                        error = error
                    )
                    LoginButton(
                        enabled = loginButtonEnabled,
                        onLogin = { onLogin(LoginProvider.Email) }
                    )
                    LoginGoogleButton(
                        onGoogleLogin = { intentResult ->
                            val idToken = googleAuthClient.getIdTokenFromResult(intentResult)
                            onLogin(LoginProvider.Google(idToken))
                        },
                        googleAuthClient = googleAuthClient
                    )
                    ToRegisterButton(
                        onRegister = onRegister
                    )
                }
            )

        }
    )
}