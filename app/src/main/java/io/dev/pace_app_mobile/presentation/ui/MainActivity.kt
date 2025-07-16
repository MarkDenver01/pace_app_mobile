package io.dev.pace_app_mobile.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.navigation.loginGraph
import io.dev.pace_app_mobile.navigation.startGraph
import io.dev.pace_app_mobile.navigation.titleGraph
import io.dev.pace_app_mobile.presentation.theme.Pace_app_mobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pace_app_mobileTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.START_ROUTE
                ) {
                    startGraph(navController)
                    titleGraph(navController)
                    loginGraph(navController)
                }
            }
        }
    }
}