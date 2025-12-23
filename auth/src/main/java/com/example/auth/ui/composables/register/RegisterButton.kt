package com.example.auth.ui.composables.register

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.R

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    onRegister: () -> Unit,
    enabled: Boolean
) {
    Button(
        enabled = enabled,
        onClick = onRegister,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        Text(stringResource(R.string.register_button), fontSize = 16.sp)
    }
}