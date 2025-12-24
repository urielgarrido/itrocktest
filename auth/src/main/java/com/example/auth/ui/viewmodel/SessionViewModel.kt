package com.example.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.usecase.LogoutUserUseCase
import com.example.auth.ui.events.SessionUIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _sessionUIEvents = MutableStateFlow<SessionUIEvents?>(null)
    val sessionUIEvents = _sessionUIEvents.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase().onEach { result ->
                if (result.isSuccess) {
                    onLogout()
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> throw throwable
                }
            }.collect()

        }
    }

    private fun onLogout() {
        _sessionUIEvents.value = SessionUIEvents.OnLogout
    }

    fun resetSessionUIEvents() {
        _sessionUIEvents.value = null
    }
}