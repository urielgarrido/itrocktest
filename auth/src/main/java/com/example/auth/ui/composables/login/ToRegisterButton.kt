package com.example.auth.ui.composables.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.example.auth.R

@Composable
fun ToRegisterButton(
    modifier: Modifier = Modifier,
    onRegister: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.do_you_dont_have_account))

        withLink(
            LinkAnnotation.Clickable(
                tag = "registrate",
                styles = TextLinkStyles(
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
            append(stringResource(R.string.register_button))
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier.padding(16.dp)
    )
}