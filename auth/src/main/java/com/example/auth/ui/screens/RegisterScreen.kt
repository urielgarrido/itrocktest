package com.example.auth.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.R

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    toLogin: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = stringResource(R.string.register_header))
                    RegisterFields(
                        email = email,
                        onEmailChange = onEmailChange,
                        password = password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = passwordVisible,
                        onPasswordVisibleChange = onPasswordVisibleChange,
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        confirmPasswordVisible = confirmPasswordVisible,
                        onConfirmPasswordVisibleChange = onConfirmPasswordVisibleChange
                    )
                    RegisterButton(
                        onRegister = onRegister
                    )
                    ToLoginTextButton(
                        toLogin = toLogin
                    )
                }
            )
        }
    )

}

@Composable
fun RegisterFields(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibleChange: (Boolean) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                Icon(
                    painter = if (passwordVisible) painterResource(id = R.drawable.visibility)
                    else painterResource(id = R.drawable.visibility_off),
                    contentDescription = null
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { onConfirmPasswordChange(it) },
        label = { Text("Confirmar Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) }) {
                Icon(
                    painter = if (confirmPasswordVisible) painterResource(id = R.drawable.visibility)
                    else painterResource(id = R.drawable.visibility_off),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun RegisterButton(modifier: Modifier = Modifier, onRegister: () -> Unit) {
    Button(
        onClick = onRegister,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Registrarse", fontSize = 18.sp)
    }
}

@Composable
fun ToLoginTextButton(modifier: Modifier = Modifier, toLogin: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append("¿Ya tienes cuenta? ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "login",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { toLogin() }
            )
        ) {
            append("Inicia sesión")
        }
    }

    Text(text = annotatedString)
}