package com.example.testitrock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.core.theme.TestITRockTheme
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys
import com.example.testitrock.ui.NavigationRoot
import com.example.testitrock.ui.viewmodel.NavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestITRockTheme {
                val navigationViewModel = hiltViewModel<NavViewModel>()
                val startDestination = navigationViewModel.startDestination.collectAsStateWithLifecycle()
                if (startDestination.value != null) {
                    NavigationRoot(startDestination.value!!)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}