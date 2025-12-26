package com.example.auth.ui.utils

import android.util.Patterns
import com.example.auth.domain.validation.EmailValidator
import javax.inject.Inject

class AndroidEmailValidator @Inject constructor() : EmailValidator {
    override fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }
}