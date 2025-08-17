package io.dev.pace_app_mobile.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.navigation.assessmentGraph
import io.dev.pace_app_mobile.navigation.loginGraph
import io.dev.pace_app_mobile.navigation.signUpGraph
import io.dev.pace_app_mobile.navigation.startGraph
import io.dev.pace_app_mobile.navigation.titleGraph
import io.dev.pace_app_mobile.presentation.theme.Pace_app_mobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
                    signUpGraph(navController)
                    assessmentGraph(navController)
                }
            }
        }
    }
}