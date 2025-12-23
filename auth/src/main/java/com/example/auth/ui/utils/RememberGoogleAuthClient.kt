package com.example.auth.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.auth.di.GoogleAuthEntryPoint
import com.example.auth.domain.repository.GoogleAuthClient
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberGoogleAuthClient(): GoogleAuthClient {
    val context = LocalContext.current.applicationContext
    return remember {
        EntryPointAccessors
            .fromApplication(context, GoogleAuthEntryPoint::class.java)
            .googleAuthClient()
    }
}
