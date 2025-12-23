package com.example.auth.data.repository

import android.content.Intent
import com.example.auth.domain.repository.GoogleAuthClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class GoogleAuthClientImpl @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) : GoogleAuthClient {

    override fun signInIntent(): Intent =
        googleSignInClient.signInIntent

    override fun getIdTokenFromResult(intent: Intent): String {
        val account = GoogleSignIn
            .getSignedInAccountFromIntent(intent)
            .getResult(ApiException::class.java)

        return account.idToken ?: throw NullPointerException("idToken is null")
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }
}