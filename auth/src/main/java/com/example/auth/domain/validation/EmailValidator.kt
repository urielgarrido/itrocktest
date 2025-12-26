package com.example.auth.domain.validation

interface EmailValidator {
    fun isValid(email: String): Boolean
}