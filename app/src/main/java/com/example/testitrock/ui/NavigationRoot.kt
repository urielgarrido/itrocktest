package com.example.testitrock.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.auth.ui.screens.LoginScreen
import com.example.auth.ui.screens.RegisterScreen
import com.example.auth.ui.viewmodel.LoginViewModel
import com.example.auth.ui.viewmodel.RegisterViewModel
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(
        AuthNavKeys.Login,
        AuthNavKeys.Register,
        ProductNavKeys.Home,
        ProductNavKeys.Details,
        ProductNavKeys.History,
        ProductNavKeys.Payment
    )

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<AuthNavKeys.Login> {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val loginState = loginViewModel.loginState.collectAsStateWithLifecycle()
                LoginScreen(
                    countries = loginState.value.countries,
                    onCountrySelected = loginViewModel::updateCountrySelected,
                    email = loginState.value.email,
                    onEmailChange = loginViewModel::updateEmail,
                    password = loginState.value.password,
                    onPasswordChange = loginViewModel::updatePassword,
                    passwordVisible = loginState.value.passwordVisible,
                    onPasswordVisibleChange = loginViewModel::updatePasswordVisible,
                    onLogin = loginViewModel::login,
                    onRegister =  {
                        backStack.add(AuthNavKeys.Register)
                    }
                )
            }
            entry<AuthNavKeys.Register> {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val registerState = registerViewModel.registerState.collectAsStateWithLifecycle()
                RegisterScreen(
                    email = registerState.value.email,
                    onEmailChange = registerViewModel::updateEmail,
                    password = registerState.value.password,
                    onPasswordChange = registerViewModel::updatePassword,
                    passwordVisible = registerState.value.passwordVisible,
                    onPasswordVisibleChange = registerViewModel::updatePasswordVisible,
                    confirmPassword = registerState.value.confirmPassword,
                    onConfirmPasswordChange = registerViewModel::updateConfirmPassword,
                    confirmPasswordVisible = registerState.value.confirmPasswordVisible,
                    onConfirmPasswordVisibleChange = registerViewModel::updateConfirmPasswordVisible,
                    onRegister = registerViewModel::register,
                    toLogin = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<ProductNavKeys.Home> {
                Text(text = "Home")
            }
            entry<ProductNavKeys.Details> {
                Text(text = "Details")
            }
            entry<ProductNavKeys.History> {
                Text(text = "History")
            }
            entry<ProductNavKeys.Payment> {
                Text(text = "Payment")
            }
        }
    )

}