package com.example.auth.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
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
import com.example.auth.ui.utils.LoginProvider

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    countries: List<String>,
    onCountrySelected: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    onLogin: (LoginProvider) -> Unit,
    onRegister: () -> Unit
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
                    Text(
                        text = stringResource(R.string.login_header)
                    )
                    CountrySelector(
                        countries = countries,
                        onCountrySelected = onCountrySelected
                    )
                    LoginFields(
                        email = email,
                        onEmailChange = onEmailChange,
                        password = password,
                        onPasswordChange = onPasswordChange,
                        passwordVisible = passwordVisible,
                        onVisiblePasswordChange = onPasswordVisibleChange
                    )
                    LoginButton(
                        onLogin = { onLogin(LoginProvider.EMAIL) }
                    )
                    LoginGoogleButton(
                        onGoogleLogin = { onLogin(LoginProvider.GOOGLE) }
                    )
                    ToRegisterButton(
                        onRegister = onRegister
                    )
                }
            )

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelector(
    modifier: Modifier = Modifier,
    countries: List<String>,
    onCountrySelected: (String) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
        content = {
            OutlinedTextField(
                value = countries.first(),
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(stringResource(R.string.select_a_country))
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun LoginFields(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisiblePasswordChange: (Boolean) -> Unit,
) {

    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text("Email") },
        placeholder = { Text("ejemplo@correo.com") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )

    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                ImageVector.vectorResource(R.drawable.visibility)
            else ImageVector.vectorResource(R.drawable.visibility_off)

            val contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { onVisiblePasswordChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = contentDescription)
            }
        }
    )

}

@Composable
fun LoginGoogleButton(
    modifier: Modifier = Modifier,
    onGoogleLogin: () -> Unit
) {
    OutlinedButton(
        onClick = onGoogleLogin,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Continuar con Google",
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit
) {
    Button(
        onClick = onLogin,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Iniciar Sesión",
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_forward),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ToRegisterButton(
    modifier: Modifier = Modifier,
    onRegister: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append("¿No tienes cuenta? ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "registrate",
                styles = androidx.compose.ui.text.TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    onRegister()
                }
            )
        ) {
            append("Registrate")
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier.padding(16.dp)
    )
}