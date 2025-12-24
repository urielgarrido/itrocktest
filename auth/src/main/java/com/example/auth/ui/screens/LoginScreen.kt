package com.example.auth.ui.screens

import android.content.Intent
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
import com.example.auth.ui.composables.login.CountrySelector
import com.example.auth.ui.composables.login.LoginButton
import com.example.auth.ui.composables.login.LoginFields
import com.example.auth.ui.composables.login.LoginGoogleButton
import com.example.auth.ui.composables.login.ToRegisterButton
import com.example.auth.ui.events.LoginUIEvents
import com.example.auth.ui.states.LoginState
import com.example.auth.ui.utils.LoginProvider
import com.example.core.domain.models.Country

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginState: LoginState,
    onCountrySelected: (Country) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit,
    onLogin: (LoginProvider) -> Unit,
    onRegister: () -> Unit,
    loginUIEvents: LoginUIEvents?,
    onResetLoginUIEvents: () -> Unit,
    onGoToNextScreen: () -> Unit,
    signInIntent: () -> Intent,
    getIdTokenFromResult: (Intent) -> String,
) {

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
                        countries = loginState.countries,
                        countrySelected = loginState.selectedCountry,
                        onCountrySelected = onCountrySelected
                    )
                    LoginFields(
                        email = loginState.email,
                        onEmailChange = onEmailChange,
                        password = loginState.password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = loginState.passwordVisible,
                        onVisiblePasswordChange = onPasswordVisibleChange,
                        error = loginState.error
                    )
                    LoginButton(
                        enabled = loginState.loginButtonEnabled,
                        onLogin = { onLogin(LoginProvider.Email) }
                    )
                    LoginGoogleButton(
                        onGoogleLogin = { intentResult ->
                            val idToken = getIdTokenFromResult(intentResult)
                            onLogin(LoginProvider.Google(idToken))
                        },
                        signInIntent = signInIntent
                    )
                    ToRegisterButton(
                        onRegister = onRegister
                    )
                }
            )

        }
    )
}