package com.example.testitrock.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.auth.ui.screens.LoginScreen
import com.example.auth.ui.screens.RegisterScreen
import com.example.auth.ui.viewmodel.LoginViewModel
import com.example.auth.ui.viewmodel.RegisterViewModel
import com.example.products.ui.screens.HomeScreen
import com.example.products.ui.screens.ProductDetailScreen
import com.example.products.ui.screens.PurchaseHistoryScreen
import com.example.products.ui.viewmodels.HomeViewModel
import com.example.products.ui.viewmodels.ProductDetailViewModel
import com.example.products.ui.viewmodels.PurchaseHistoryViewModel
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys

@Composable
fun NavigationRoot(startDestination: NavKey) {
    val backStack = rememberNavBackStack(startDestination)

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
                val loginUIEvents = loginViewModel.loginUIEvents.collectAsStateWithLifecycle()
                LoginScreen(
                    countries = loginState.value.countries,
                    countrySelected = loginState.value.selectedCountry,
                    onCountrySelected = loginViewModel::updateCountrySelected,
                    email = loginState.value.email,
                    onEmailChange = loginViewModel::updateEmail,
                    password = loginState.value.password,
                    onPasswordChange = loginViewModel::updatePassword,
                    passwordVisible = loginState.value.passwordVisible,
                    onPasswordVisibleChange = loginViewModel::updatePasswordVisible,
                    loginButtonEnabled = loginState.value.loginButtonEnabled,
                    onLogin = loginViewModel::login,
                    onRegister =  {
                        backStack.add(AuthNavKeys.Register)
                    },
                    error = loginState.value.error,
                    loginUIEvents = loginUIEvents.value,
                    onResetLoginUIEvents = {
                        loginViewModel.resetLoginUIEvents()
                    },
                    onGoToNextScreen = {
                        backStack.removeLastOrNull()
                        backStack.add(ProductNavKeys.Home)
                    },
                    signInIntent = loginViewModel::getSignInIntent,
                    getIdTokenFromResult = loginViewModel::getIdTokenFromResult
                )
            }
            entry<AuthNavKeys.Register> {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val registerState = registerViewModel.registerState.collectAsStateWithLifecycle()
                val registerUIEvents = registerViewModel.registerUIEvents.collectAsStateWithLifecycle()
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
                    isRegisterButtonEnabled = registerState.value.registerButtonEnabled,
                    toLogin = {
                        backStack.removeLastOrNull()
                    },
                    error = registerState.value.error,
                    registerUIEvents = registerUIEvents.value,
                    onResetRegisterUIEvents = {
                        registerViewModel.resetRegisterUIEvents()
                    },
                    onGoToNextScreen = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<AuthNavKeys.Sesion> {
                Text(text = "Sesion")
            }
            entry<ProductNavKeys.Home> {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()
                val homeUIEvents = homeViewModel.homeUIEvents.collectAsStateWithLifecycle()
                HomeScreen()
            }
            entry<ProductNavKeys.ProductDetail> {
                val productDetailViewModel = hiltViewModel<ProductDetailViewModel>()
                val productDetailState = productDetailViewModel.productDetailState.collectAsStateWithLifecycle()
                val productDetailUIEvents = productDetailViewModel.productDetailUIEvents.collectAsStateWithLifecycle()
                ProductDetailScreen()
            }
            entry<ProductNavKeys.PurchaseHistory> {
                val purchaseHistoryViewModel = hiltViewModel<PurchaseHistoryViewModel>()
                val purchaseHistoryState = purchaseHistoryViewModel.purchaseHistoryState.collectAsStateWithLifecycle()
                PurchaseHistoryScreen()
            }
            entry<ProductNavKeys.Payment> {
                Text(text = "Payment")
            }
        }
    )

}