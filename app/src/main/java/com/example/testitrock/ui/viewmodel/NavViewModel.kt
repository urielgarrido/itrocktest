package com.example.testitrock.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.example.auth.domain.usecase.GetUserUIDUseCase
import com.example.auth.domain.usecase.IsUserLoggedInUseCase
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class NavViewModel @Inject constructor(
    isUserLoggedInUseCase: IsUserLoggedInUseCase,
    getUserUIDUseCase: GetUserUIDUseCase
) : ViewModel() {

    val startDestination: StateFlow<NavKey?> =
        isUserLoggedInUseCase()
            .map { isUserLoggedIn ->
                if (isUserLoggedIn) ProductNavKeys.Home else AuthNavKeys.Login
            }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val userUID: StateFlow<String?> =
        getUserUIDUseCase()
            .distinctUntilChanged()
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}