package com.example.testitrock.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.example.auth.ui.screens.SessionScreen
import com.example.auth.ui.viewmodel.LoginViewModel
import com.example.auth.ui.viewmodel.RegisterViewModel
import com.example.auth.ui.viewmodel.SessionViewModel
import com.example.products.ui.screens.HomeScreen
import com.example.products.ui.screens.PaymentScreen
import com.example.products.ui.screens.ProductDetailScreen
import com.example.products.ui.screens.PurchaseHistoryScreen
import com.example.products.ui.viewmodels.HomeViewModel
import com.example.products.ui.viewmodels.PaymentViewModel
import com.example.products.ui.viewmodels.ProductDetailViewModel
import com.example.products.ui.viewmodels.PurchaseHistoryViewModel
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys
import com.example.testitrock.ui.models.bottomItems

@Composable
fun NavigationRoot(
    startDestination: NavKey,
    userUID: String?
) {
    val backStack = rememberNavBackStack(startDestination)

    Scaffold(
        bottomBar = {
            val currentRoute = backStack.last()
            if (bottomItems.any { it.route == currentRoute }) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    backStack.add(item.route)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(text = item.label) }
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            NavDisplay(
                modifier = Modifier.padding(paddingValues),
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
                            loginState = loginState.value,
                            onCountrySelected = loginViewModel::updateCountrySelected,
                            onEmailChange = loginViewModel::updateEmail,
                            onPasswordChange = loginViewModel::updatePassword,
                            onPasswordVisibleChange = loginViewModel::updatePasswordVisible,
                            onLogin = loginViewModel::login,
                            onRegister = {
                                backStack.add(AuthNavKeys.Register)
                            },
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
                            registerState = registerState.value,
                            onEmailChange = registerViewModel::updateEmail,
                            onPasswordChange = registerViewModel::updatePassword,
                            onPasswordVisibleChange = registerViewModel::updatePasswordVisible,
                            onConfirmPasswordChange = registerViewModel::updateConfirmPassword,
                            onConfirmPasswordVisibleChange = registerViewModel::updateConfirmPasswordVisible,
                            onRegister = registerViewModel::register,
                            toLogin = {
                                backStack.removeLastOrNull()
                            },
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
                        val sessionViewModel = hiltViewModel<SessionViewModel>()
                        val sessionUIEvents = sessionViewModel.sessionUIEvents.collectAsStateWithLifecycle()
                        SessionScreen(
                            sessionUIEvents = sessionUIEvents.value,
                            onLogout = sessionViewModel::logout,
                            onGoToInitScreen = {
                                backStack.removeAll(backStack.toList())
                                backStack.add(AuthNavKeys.Login)
                            },
                            onResetSessionUIEvents = sessionViewModel::resetSessionUIEvents
                        )
                    }
                    entry<ProductNavKeys.Home> {
                        val homeViewModel = hiltViewModel<HomeViewModel>()
                        val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()
                        val homeUIEvents = homeViewModel.homeUIEvents.collectAsStateWithLifecycle()
                        HomeScreen(
                            homeState = homeState.value,
                            homeUIEvents = homeUIEvents.value,
                            getAllProducts = homeViewModel::getAllProducts,
                            onProductClick = homeViewModel::onProductClick,
                            onResetHomeUIEvents = homeViewModel::resetHomeUIEvents,
                            onGoToProductDetail = { productId ->
                                backStack.add(ProductNavKeys.ProductDetail(productId))
                            }
                        )
                    }
                    entry<ProductNavKeys.ProductDetail> {
                        val productId = it.productId
                        val productDetailViewModel = hiltViewModel<ProductDetailViewModel>()
                        val productDetailState = productDetailViewModel.productDetailState.collectAsStateWithLifecycle()
                        val productDetailUIEvents = productDetailViewModel.productDetailUIEvents.collectAsStateWithLifecycle()
                        ProductDetailScreen(
                            productId = productId,
                            productDetailState = productDetailState.value,
                            productDetailUIEvents = productDetailUIEvents.value,
                            getProductDetail = productDetailViewModel::getProductDetail,
                            onProductWant = productDetailViewModel::onProductWant,
                            onResetProductDetailUIEvents = productDetailViewModel::resetProductDetailUIEvents,
                            onGoToPayment = {
                                backStack.add(ProductNavKeys.Payment)
                            }
                        )
                    }
                    entry<ProductNavKeys.Payment> {
                        val paymentViewModel = hiltViewModel<PaymentViewModel>()
                        val paymentState = paymentViewModel.paymentState.collectAsStateWithLifecycle()
                        val paymentUIEvents = paymentViewModel.paymentUIEvents.collectAsStateWithLifecycle()
                        PaymentScreen(
                            paymentState = paymentState.value,
                            paymentUIEvents = paymentUIEvents.value,
                            onCardNumberChange = paymentViewModel::updateCardNumber,
                            onCardHolderChange = paymentViewModel::updateCardHolder,
                            onExpirationDateChange = paymentViewModel::updateExpirationDate,
                            onCVVChange = paymentViewModel::updateCVV,
                            onResetPaymentUIEvents = paymentViewModel::resetPaymentUIEvents,
                            onBuyClick = {
                                paymentViewModel.buyProduct(userUID!!)
                            },
                            onGoToNextScreen = {
                                backStack.removeLastOrNull()

                            }
                        )
                    }
                    entry<ProductNavKeys.PurchaseHistory> {
                        val purchaseHistoryViewModel = hiltViewModel<PurchaseHistoryViewModel>()
                        val purchaseHistoryState = purchaseHistoryViewModel.purchaseHistoryState.collectAsStateWithLifecycle()
                        PurchaseHistoryScreen()
                    }
                }
            )
        }
    )
}