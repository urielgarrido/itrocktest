package com.example.auth.domain.repository

import android.content.Intent

interface GoogleAuthClient {
    fun signInIntent(): Intent
    fun getIdTokenFromResult(intent: Intent): String
    suspend fun signOut()
}