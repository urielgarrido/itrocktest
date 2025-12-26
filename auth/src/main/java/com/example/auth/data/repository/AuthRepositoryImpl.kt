package com.example.auth.data.repository

import com.example.auth.domain.exceptions.LoginExceptions
import com.example.auth.domain.exceptions.RegisterExceptions
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.domain.repository.GoogleAuthClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthClient: GoogleAuthClient
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Result<Unit>> = flow {
        runCatching {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(LoginExceptions.InvalidCredentials))
            }
        }.onFailure { throwable ->
            when (throwable) {
                is FirebaseException -> {
                    val isInvalidLoginCredentials = throwable.message?.contains("INVALID_LOGIN_CREDENTIALS")
                    if (isInvalidLoginCredentials == true) {
                        throw LoginExceptions.InvalidCredentials
                    } else throw throwable
                }

                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun loginWithGoogle(idToken: String): Flow<Result<Unit>> = flow {
        runCatching {
            val credentials = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credentials).await()
            if (authResult.user != null) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(LoginExceptions.InvalidCredentials))
            }
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun register(email: String, password: String): Flow<Result<Unit>> = flow {
        runCatching {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(RegisterExceptions.RegisterUserFailed))
            }
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout(): Flow<Result<Unit>> = flow {
        runCatching {
            firebaseAuth.signOut()
            googleAuthClient.signOut()
            emit(Result.success(Unit))
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }

    override val isUserLoggedIn: Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)

    override val userUID: Flow<String?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)

    override val userEmail: Flow<String?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(firebaseAuth.currentUser?.email)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)
}