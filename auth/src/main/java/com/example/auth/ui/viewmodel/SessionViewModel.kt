package com.example.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.usecase.GetUserEmailUseCase
import com.example.auth.domain.usecase.LogoutUserUseCase
import com.example.auth.ui.errors.SessionError
import com.example.auth.ui.events.SessionUIEvents
import com.example.auth.ui.states.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase
) : ViewModel() {

    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState = _sessionState.asStateFlow()

    private val _sessionUIEvents = MutableStateFlow<SessionUIEvents?>(null)
    val sessionUIEvents = _sessionUIEvents.asStateFlow()

    fun getUserEmail() {
        viewModelScope.launch {
            getUserEmailUseCase().onEach { userEmail ->
                if (userEmail != null) {
                    _sessionState.update {
                        it.copy(
                            userEmail = userEmail
                        )
                    }
                } else {
                    onSessionError(SessionError.OnGetEmailError)
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> onSessionError(SessionError.OnGetEmailError)
                }
            }.collect()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase().onEach { result ->
                if (result.isSuccess) {
                    onLogout()
                } else {
                    onSessionError(SessionError.OnLogoutError)
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> onSessionError(SessionError.OnLogoutError)
                }
            }.collect()

        }
    }

    private fun onSessionError(error: SessionError) {
        _sessionState.update {
            it.copy(
                error = error
            )
        }
    }

    private fun onLogout() {
        _sessionUIEvents.value = SessionUIEvents.OnLogout
    }

    fun resetSessionUIEvents() {
        _sessionUIEvents.value = null
    }
}